/*******************************************************************************
 * Copyright (c) 2001, 2017 IBM Corp. and others
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
package com.ibm.uma.freemarker;

import java.util.Iterator;
import java.util.Set;

import com.ibm.uma.UMA;
import com.ibm.uma.UMAException;

import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateModelIterator;

public class Flags implements TemplateCollectionModel, TemplateHashModel {
	
	Set<String> flags;
	
	public Flags() {
		flags = UMA.getUma().getConfiguration().getAllFlags();
	}
	
	public Flags(Set<String> flags) {
		this.flags = flags;
	}
	
	class FlagsIterator implements TemplateModelIterator {
		Iterator<String> iterator;
	
		public FlagsIterator() {
			iterator = flags.iterator();
		}
		public boolean hasNext() throws TemplateModelException {
			return iterator.hasNext();
		}
		public TemplateModel next() throws TemplateModelException {
			try {
				return new Flag(iterator.next());
			} catch (UMAException e) {
				throw new TemplateModelException(e);
			}			
		}
	}

	public TemplateModelIterator iterator() throws TemplateModelException {
		return new FlagsIterator();
	}
	
	
	public TemplateModel get(String arg0) throws TemplateModelException {
		try {
			return new Flag(arg0);
		} catch (UMAException e) {
			throw new TemplateModelException(e);
		}
	}

	public boolean isEmpty() throws TemplateModelException {
		return false;
	}
	
	

}
