/*******************************************************************************
 * Copyright (c) 2017, 2018 IBM Corp. and others
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
package org.openj9.test.java.lang.management.ThreadMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/* getThreadInfo() for a thread that has terminated */
class DeadThread extends ThreadMXBeanTestCase {
	@Parameters( {"repeatcountPara"} )
	@Test(groups = { "level.extended" })
	public void testDeadThread(@Optional("100") String repeatcountPara) {
		int repeatCount = Integer.parseInt(repeatcountPara);
		
		for (int i = 0; i < repeatCount; i++) {
			this.runTest();
		}

		Assert.assertEquals(getExitStatus(), ExitStatus.PASS);
	}

	public void runTest() {
		ThreadMXBean mxb = ManagementFactory.getThreadMXBean();
		Thread t = new Thread();
		long id = t.getId();
		try {
			t.start();
			t.join();

			if (mxb.getThreadInfo(id, 0) != null) {
				this.setFailed();
			}
		} catch (InterruptedException ie) {
			ie.printStackTrace();
			this.setFailed();
		}
	}
}
