/*******************************************************************************
 * Copyright (c) 2001, 2018 IBM Corp. and others
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
#include <stdlib.h>
#include <string.h>

#include "ibmjvmti.h"
#include "jvmti_test.h"

static agentEnv * env;

jint JNICALL
glc001(agentEnv * agent_env, char * args)
{
	JVMTI_ACCESS_FROM_AGENT(agent_env);

	env = agent_env;

	return JNI_OK;
}

jboolean JNICALL
Java_com_ibm_jvmti_tests_getLoadedClasses_glc001_check(JNIEnv *jni_env, jclass cls)
{
	JVMTI_ACCESS_FROM_AGENT(env);
    jint        classCount = 0;
	jclass    * pClass;
    jvmtiError  err;

	err = (*jvmti_env)->GetLoadedClasses(jvmti_env, &classCount, &pClass);
	if (err != JVMTI_ERROR_NONE) {
		error(env, err, "GetLoadedClasses() failed");
		return JNI_FALSE;
	}

	if (classCount == 0) {
		error(env, err, "GetLoadedClasses() for a class failed");
		return JNI_FALSE;
	}

	printf("\tclassCount=%d\n", classCount);

    return JNI_TRUE;
}
