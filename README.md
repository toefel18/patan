# Small Java library for sampling code.

![travis-ci](https://travis-ci.org/toefel18/patan.svg?branch=master "build")


There are multiple ways for measuring the performance of code, sampling and instrumentation. Instrumentation modifies the original program's byte code by inserting measurement code. Sampling is done by programming the measurement code manually. Both methods work.

## Goals
This library provides an interface with implementation that you can use to measure
execution time of tasks (not limited to a single method!) or count occurrences of events. The collected data can be retrieved for analysis, for example by writing it to a log file or exposing it through a REST/JSON API.
