/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.mygdx.game.core.util;

import java.util.Arrays;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

/** A quad tree that stores a int for each point.
 * @author Nathan Sweet */
public class QuadTreeInt implements Poolable {
	static public final int VALUE = 0, X = 1, Y = 2, DISTSQR = 3;

	static private final Pool<QuadTreeInt> pool = new Pool(128, 4096) {
		protected Object newObject () {
			return new QuadTreeInt();
		}
	};

	public final int maxValues, maxDepth;
	public int x, y, width, height;
	public int depth;
	public @Null QuadTreeInt nw, ne, sw, se;

	/** For each entry, stores the value, x, and y. */
	public int[] values;

	/** The number of elements stored in {@link #values} (3 values per quad tree entry). */
	public int count;

	/** Creates a quad tree with 16 for maxValues and 8 for maxDepth. */
	public QuadTreeInt() {
		this(16, 8);
	}

	/** @param maxValues The maximum number of values stored in each quad tree node. When exceeded, the node is split into 4 child
	 *           nodes. If the maxDepth has been reached, more than maxValues may be stored.
	 * @param maxDepth The maximum depth of the tree nodes. Nodes at the maxDepth will not be split and may store more than
	 *           maxValues number of entries. */
	public QuadTreeInt(int maxValues, int maxDepth) {
		this.maxValues = maxValues * 3;
		this.maxDepth = maxDepth;
		values = new int[this.maxValues];
	}

	public void setBounds (int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void add (int value, int valueX, int valueY) {
		int count = this.count;
		if (count == -1) {
			addToChild(value, valueX, valueY);
			return;
		}
		if (depth < maxDepth) {
			if (count == maxValues) {
				split(value, valueX, valueY);
				return;
			}
		} else if (count == values.length) //
			values = Arrays.copyOf(values, growValues());
		values[count] = value;
		values[count + 1] = valueX;
		values[count + 2] = valueY;
		this.count += 3;
	}

	private void split (int value, int valueX, int valueY) {
		int[] values = this.values;
		for (int i = 0; i < maxValues; i += 3)
			addToChild(values[i], values[i + 1], values[i + 2]);
		// values isn't nulled because the trees are pooled.
		count = -1;
		addToChild(value, valueX, valueY);
	}

	private void addToChild (int value, int valueX, int valueY) {
		QuadTreeInt child;
		int halfWidth = width / 2, halfHeight = height / 2;
		if (valueX < x + halfWidth) {
			if (valueY < y + halfHeight)
				child = sw != null ? sw : (sw = obtainChild(x, y, halfWidth, halfHeight, depth + 1));
			else
				child = nw != null ? nw : (nw = obtainChild(x, y + halfHeight, halfWidth, halfHeight, depth + 1));
		} else {
			if (valueY < y + halfHeight)
				child = se != null ? se : (se = obtainChild(x + halfWidth, y, halfWidth, halfHeight, depth + 1));
			else
				child = ne != null ? ne : (ne = obtainChild(x + halfWidth, y + halfHeight, halfWidth, halfHeight, depth + 1));
		}
		child.add(value, valueX, valueY);
	}

	private QuadTreeInt obtainChild (int x, int y, int width, int height, int depth) {
		QuadTreeInt child = pool.obtain();
		child.x = x;
		child.y = y;
		child.width = width;
		child.height = height;
		child.depth = depth;
		return child;
	}

	/** Returns a new length for {@link #values} when it is not enough to hold all the entries after {@link #maxDepth} has been
	 * reached. */
	protected int growValues () {
		return count + 10 * 3;
	}

	/** @param results For each entry found within the radius, if any, the value, x, y, and square of the distance to the entry are
	 *           added to this array. See {@link #VALUE}, {@link #X}, {@link #Y}, and {@link #DISTSQR}. */
	public void query (int centerX, int centerY, int radius, IntArray results) {
		query(centerX, centerY, radius * radius, centerX - radius, centerY - radius, radius * 2, results);
	}

	private void query (int centerX, int centerY, int radiusSqr, int rectX, int rectY, int rectSize,
		IntArray results) {
		if (!(x < rectX + rectSize && x + width > rectX && y < rectY + rectSize && y + height > rectY)) return;
		int count = this.count;
		if (count != -1) {
			int[] values = this.values;
			for (int i = 1; i < count; i += 3) {
				int px = values[i], py = values[i + 1];
				int dx = px - centerX, dy = py - centerY;
				int d = dx * dx + dy * dy;
				if (d <= radiusSqr) {
					results.add(values[i - 1]);
					results.add(px);
					results.add(py);
					results.add(d);
				}
			}
		} else {
			if (nw != null) nw.query(centerX, centerY, radiusSqr, rectX, rectY, rectSize, results);
			if (sw != null) sw.query(centerX, centerY, radiusSqr, rectX, rectY, rectSize, results);
			if (ne != null) ne.query(centerX, centerY, radiusSqr, rectX, rectY, rectSize, results);
			if (se != null) se.query(centerX, centerY, radiusSqr, rectX, rectY, rectSize, results);
		}
	}

	/** @param result For the entry nearest to the specified point, the value, x, y, and square of the distance to the value are
	 *           added to this array after it is cleared. See {@link #VALUE}, {@link #X}, {@link #Y}, and {@link #DISTSQR}.
	 * @return false if no entry was found because the quad tree was empty or the specified point is farther than the larger of the
	 *         quad tree's width or height from an entry. If false is returned the result array is empty. */
	public boolean nearest (int x, int y, IntArray result) {
		// Find nearest value in a cell that contains the point.
		result.clear();
		result.add(0);
		result.add(0);
		result.add(0);
		result.add(Integer.MAX_VALUE);
		findNearestInternal(x, y, result);
		int nearValue = result.first(), nearX = result.get(1), nearY = result.get(2), nearDist = result.get(3);
		boolean found = nearDist != Integer.MAX_VALUE;
		if (!found) {
			nearDist = Math.max(width, height);
			nearDist *= nearDist;
		}

		// Check for a nearer value in a neighboring cell.
		result.clear();
		query(x, y, (int)Math.sqrt(nearDist), result);
		for (int i = 3, n = result.size; i < n; i += 4) {
			int dist = result.get(i);
			if (dist < nearDist) {
				nearDist = dist;
				nearValue = result.get(i - 3);
				nearX = result.get(i - 2);
				nearY = result.get(i - 1);
			}
		}
		if (!found && result.isEmpty()) return false;
		result.clear();
		result.add(nearValue);
		result.add(nearX);
		result.add(nearY);
		result.add(nearDist);
		return true;
	}

	private void findNearestInternal (int x, int y, IntArray result) {
		if (!(this.x < x && this.x + width > x && this.y < y && this.y + height > y)) return;

		int count = this.count;
		if (count != -1) {
			int nearValue = result.first(), nearX = result.get(1), nearY = result.get(2), nearDist = result.get(3);
			int[] values = this.values;
			for (int i = 1; i < count; i += 3) {
				int px = values[i], py = values[i + 1];
				int dx = px - x, dy = py - y;
				int dist = dx * dx + dy * dy;
				if (dist < nearDist) {
					nearDist = dist;
					nearValue = values[i - 1];
					nearX = px;
					nearY = py;
				}
			}
			result.set(0, nearValue);
			result.set(1, nearX);
			result.set(2, nearY);
			result.set(3, nearDist);
		} else {
			if (nw != null) nw.findNearestInternal(x, y, result);
			if (sw != null) sw.findNearestInternal(x, y, result);
			if (ne != null) ne.findNearestInternal(x, y, result);
			if (se != null) se.findNearestInternal(x, y, result);
		}
	}

	public void reset () {
		if (count == -1) {
			if (nw != null) {
				pool.free(nw);
				nw = null;
			}
			if (sw != null) {
				pool.free(sw);
				sw = null;
			}
			if (ne != null) {
				pool.free(ne);
				ne = null;
			}
			if (se != null) {
				pool.free(se);
				se = null;
			}
		}
		count = 0;
		if (values.length > maxValues) values = new int[maxValues];
	}
}
