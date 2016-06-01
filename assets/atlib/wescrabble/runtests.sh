#!/bin/bash

IAT=./iat

selector='*'
[[ -z "$1" ]] || selector=$1

sedcmd='s/tests\/(.+)\.at/~.tests.\1.runTest(~.tests.reporter());/'
find tests/Test$selector.at | sed -E $sedcmd | ${IAT} -q | grep -Ev '^nil$'
