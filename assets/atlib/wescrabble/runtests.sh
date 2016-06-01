#!/bin/bash

IAT=./iat
sedcmd='s/tests\/(.+)\.at/~.tests.\1.runTest(~.tests.reporter());/'
find tests/Test*.at | sed -E $sedcmd | ${IAT} -q | grep -Ev '^nil$'
