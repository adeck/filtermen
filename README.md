# CSE462 Project Repo: #

## The following pipelines are in this repository: ##

<!-- For anyone new to markdown, the numbers used in this list don't actually matter for most compilers; they simply boil down to <li> tages within an <ol>. However, use sane numbers, anyway, since this does vary (and may change in future releases). -->

1. Polarized Image Pipeline for Prof. Viktor Gruev
2. Gamma-Ray Telescope Data Compression (VERITAS) for Prof. Mark Franklin

## Directory structure: ##

Is the same for both pipelines (will be slightly different when updated to use maven, but most of the changes will be made above the level of main and test).

It is as follows:

* pipeline\_name
  * main
    contains all code necessary for the complete application, including
    helpful functions and the entry point.
    * kernels
      Contains all the kernels, or "functional blocks" of the application.
  * test
    Contains all the tests; one for each kernel at minimum.
  * related
    Contains anything related to, but not explicitly part of, the
    application and its tests.

## Pipeline Structure: ##

Each of the individual kernels should be described in its own file. Main files (i.e. Main.scala) describe the entire application, and import individual kernels as necessary.

Kernels should go in the 'kernels' directory, and tests for each kernel should go in 'tests'. 
New kernels should enumerate the config parameters they use in a comment at the top of the file, and for any parameter with a vague name, the meaning of that parameter should be clarified. 
This is encouraged so that we don't end up needing to define 40 different cryptically-named parameters for the same information in each Main file.

## Other Files ##

IO.md -- Gives inputs and outputs for each kernel

