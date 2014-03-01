# CSE462 Project Repo: #

## The following pipelines are in this repository: ##

<!-- For anyone new to markdown, the numbers used in this list don't actually matter for most compilers; they simply boil down to <li> tages within an <ol>. However, use sane numbers, anyway, since this does vary (and may change in future releases). -->

1. Polarized Image Pipeline for Prof. Viktor Gruev
2. Gamma-Ray Telescope Data Compression (VERITAS) for Prof. Mark Franklin

## Naming Conventions: ##

For every kernel written in this repository, if that kernel is intended for VERITAS, begin the file name with the tag "VER\_" followed by the kernel name (e.g. "VER\_BorderExt.scala"). Likewise, please begin every kernel for the Polarized Image Pipeline with the tag "PolPipe\_".

## Pipeline Structure: ##

Each of the individual kernels should be described in its own file. Main files (i.e. VER\_Main.scala and PolPipe\_Main.scala) will act as final application description importing the individual kernels as necessary.
