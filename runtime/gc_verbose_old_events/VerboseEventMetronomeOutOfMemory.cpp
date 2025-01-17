/*******************************************************************************
 * Copyright (c) 1991, 2019 IBM Corp. and others
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

#include "VerboseEventMetronomeOutOfMemory.hpp"
#include "GCExtensions.hpp"
#include "VerboseEventStream.hpp"
#include "VerboseManagerOld.hpp"

#include <string.h>

#if defined(J9VM_GC_REALTIME)

/**
 * Create an new instance of a MM_VerboseEventMetronomeOutOfMemory event.
 * @param event Pointer to a structure containing the data passed over the hookInterface
 */
MM_VerboseEvent *
MM_VerboseEventMetronomeOutOfMemory::newInstance(MM_OutOfMemoryEvent *event, J9HookInterface** hookInterface)
{
	MM_VerboseEventMetronomeOutOfMemory *eventObject = (MM_VerboseEventMetronomeOutOfMemory *)MM_VerboseEvent::create(event->currentThread, sizeof(MM_VerboseEventMetronomeOutOfMemory));
	if(NULL != eventObject) {
		new(eventObject) MM_VerboseEventMetronomeOutOfMemory(event, hookInterface);
		eventObject->initialize(event);
	}
	return eventObject;
}

void
MM_VerboseEventMetronomeOutOfMemory::initialize(MM_OutOfMemoryEvent *event)
{
	OMRPORT_ACCESS_FROM_OMRVMTHREAD(_omrThread);
	_timeInMilliSeconds = omrtime_current_time_millis();
	size_t stringLength = strlen(event->memorySpaceString);
	if (stringLength > sizeof(_memorySpaceString) - 1) {
		stringLength = sizeof(_memorySpaceString) - 1;
	}
	memcpy(_memorySpaceString, event->memorySpaceString, stringLength);
	_memorySpaceString[stringLength] = '\0';
}

/**
 * Populate events data fields.
 * The event calls the event stream requesting the address of events it is interested in.
 * When an address is returned it populates itself with the data.
 */
void
MM_VerboseEventMetronomeOutOfMemory::consumeEvents()
{
}

/**
 * Passes a format string and data to the output routine defined in the passed output agent.
 * @param agent Pointer to an output agent.
 */
void
MM_VerboseEventMetronomeOutOfMemory::formattedOutput(MM_VerboseOutputAgent *agent)
{
	OMRPORT_ACCESS_FROM_OMRVMTHREAD(_omrThread);
	MM_GCExtensions *extensions = MM_GCExtensions::getExtensions(_omrThread->_vm);
	MM_VerboseManagerBase *manager = extensions->verboseGCManager;
	char timestamp[32];

	omrstr_ftime(timestamp, sizeof(timestamp), VERBOSEGC_DATE_FORMAT, _timeInMilliSeconds);

	agent->formatAndOutput(static_cast<J9VMThread*>(_omrThread->_language_vmthread), manager->getIndentLevel(), "<event details=\"out of memory\" timestamp=\"%s\" memoryspace=\"%s\" J9MemorySpace=\"0x%p\" />",
		timestamp,
		_memorySpaceString,
		_memorySpace
	);
	agent->endOfCycle(static_cast<J9VMThread*>(_omrThread->_language_vmthread));
}

#endif /* J9VM_GC_REALTIME */
