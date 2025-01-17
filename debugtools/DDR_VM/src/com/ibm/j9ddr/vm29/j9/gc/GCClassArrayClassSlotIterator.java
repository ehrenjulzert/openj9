/*******************************************************************************
 * Copyright (c) 2001, 2019 IBM Corp. and others
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which accompanies this
 * distribution and is available at https://www.eclipse.org/legal/epl-2.0/
 * or the Apache License, Version 2.0 which accompanies this distribution and
 * is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * This Source Code may also be made available under the following
 * Secondary Licenses when the conditions for such availability set
 * forth in the Eclipse Public License, v. 2.0 are satisfied: GNU
 * General Public License, version 2 with the GNU Classpath
 * Exception [1] and GNU General Public License, version 2 with the
 * OpenJDK Assembly Exception [2].
 *
 * [1] https://www.gnu.org/software/classpath/license.html
 * [2] https://openjdk.org/legal/assembly-exception.html
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0 OR GPL-2.0 WITH Classpath-exception-2.0 OR LicenseRef-GPL-2.0 WITH Assembly-exception
 *******************************************************************************/
package com.ibm.j9ddr.vm29.j9.gc;

import java.util.ArrayList;
import java.util.Iterator;

import com.ibm.j9ddr.CorruptDataException;
import com.ibm.j9ddr.vm29.pointer.VoidPointer;
import com.ibm.j9ddr.vm29.pointer.generated.J9ArrayClassPointer;
import com.ibm.j9ddr.vm29.pointer.generated.J9ClassPointer;

import static com.ibm.j9ddr.vm29.structure.J9JavaAccessFlags.J9AccClassArray;

public class GCClassArrayClassSlotIterator extends GCIterator
{
	protected Iterator<J9ClassPointer> slotIterator;
	protected Iterator<VoidPointer> addressIterator;
	
	protected GCClassArrayClassSlotIterator(J9ClassPointer clazz) throws CorruptDataException
	{
		ArrayList<J9ClassPointer> slots = new ArrayList<J9ClassPointer>();
		ArrayList<VoidPointer> addresses = new ArrayList<VoidPointer>();
		J9ClassPointer slot;
		
		slot = clazz.arrayClass(); 
		if(slot.notNull()) {
			slots.add(slot);
			addresses.add(VoidPointer.cast(clazz.arrayClassEA()));
		}
		if(clazz.romClass().modifiers().allBitsIn(J9AccClassArray)) {
			J9ArrayClassPointer arrayClass = J9ArrayClassPointer.cast(clazz);
			slot = arrayClass.componentType();
			if(slot.notNull()) {
				slots.add(slot);
				addresses.add(VoidPointer.cast(arrayClass.componentTypeEA()));
			}
			slot = arrayClass.leafComponentType();
			if(slot.notNull()) {
				slots.add(slot);
				addresses.add(VoidPointer.cast(arrayClass.leafComponentTypeEA()));
			}
		}
		slotIterator = slots.iterator();
		addressIterator = addresses.iterator();
	}

	public static GCClassArrayClassSlotIterator fromJ9Class(J9ClassPointer clazz) throws CorruptDataException
	{
		return new GCClassArrayClassSlotIterator(clazz);
	}

	public boolean hasNext()
	{
		return slotIterator.hasNext();
	}

	public J9ClassPointer next()
	{
		addressIterator.next();		// Keep iterators in sync
		return slotIterator.next();
	}
	
	public VoidPointer nextAddress()
	{
		slotIterator.next();		// Keep iterators in sync
		return addressIterator.next();
	}

}
