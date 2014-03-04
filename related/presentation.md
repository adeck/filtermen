% The Filtermen
% Andrew Deck, David D'Alessandro, Stephen Berul
% March 4, 2014

# What are we doing? #

* _VERITAS pipeline_
* _Polarization pipeline_

# What have we got done? #

* Mean - Tested!
* StdDev - Tested!
* Threshold (dummy module; works)
* Border Extension - Tested!
* Runlength Encoding - Test written
* Primary Filter Module - Test written

# How are we doing it? #

* _Incrementally_ - Setting small, acheivable goals.
* _Modularly_ - Separating kernels and kernel tests across files, relying on parameters where possible to maintain flexibility. Delegating within the group.
* _Carefully_ - Version control, issues, taking full advantage of GitHub's collaborative environment. Recognizing the importance of good testing and documentation. Performing code audits.

# What we still need to do #

* Functional tests
* Timing tests
* Optimize code
* Implement parallelism
* Documentation of testing
* Documentation

# What we still need to do (cont'd) #

* Testing of documentation
* Fitting everything together
* Removing magic numbers from code

...And, of course...

* Polarization pipeline

For specifics on this, both in terms of short- and long-term goals, [follow the project](https://github.com/adeck/filtermen).

# What we still need to know #

* How we'll calculate the threshold values
* Whether the primary filter algorithm is the final one
* Behavior of the data (although we know preliminarily to use randomized gausssians. Which, thanks to David, we have a script to easily generate)


