package com.pragmadreams.redaktor.util

import platform.Foundation.NSUUID

actual fun createUUID(): String = NSUUID().UUIDString()
