---
author: Remco Bouckaert
level: Intermediate
title: Tree Posterior Analysis Tutorial
subtitle: For post-hoc analyses
beastversion: 2.6.3
---


# Background

It can be useful to run a post-hoc analysis where a tree posterior is available in the form of a NEXUS tree log file (as produced by BEAST), for example

* when a tree posterior is available from a previous analysis, but the sequence data is not.
* when estimating epidemiological parameters and using a computationally intensive tree prior, like compartimentalised models such as MASCOT.
* when you are interested in the ancestral reconstruction of internal nodes (e.g. a discrete trait analysis (DTA)) on a large tree, and another post-hoc DTA can be done quickly afterwards.

Be aware that using a tree posterior instead of using a joint analysis has the potential to result in biased estimates of the parameters of interest. Let the tree set be 

In this tutorial, we will look at a phylogeographical reconstruction {% cite  bouckaert2015phylogeography --file master-refs.bib %} of influenze HBV through Africa and Asia.

----

# Programs used in this Exercise

### BEAST2 - Bayesian Evolutionary Analysis Sampling Trees 2

BEAST2 is a free software package for Bayesian evolutionary analysis of molecular sequences using MCMC and strictly oriented toward inference using rooted, time-measured phylogenetic trees {% cite Bouckaert2014,bouckaert2019beast --file master-refs.bib %}. This tutorial uses the BEAST2 version 2.6.3.

### BEAUti2 - Bayesian Evolutionary Analysis Utility

BEAUti2 is a graphical user interface tool for generating BEAST2 XML configuration files.

Both BEAST2 and BEAUti2 are Java programs, which means that the exact same code runs on all platforms. For us it simply means that the interface will be the same on all platforms. The screenshots used in this tutorial are taken on a Mac OS X computer; however, both programs will have the same layout and functionality on both Windows and Linux. BEAUti2 is provided as a part of the BEAST2 package so you do not need to install it separately.

### Tracer

Tracer is used to summarise the posterior estimates of the various parameters sampled by the Markov Chain. This program can be used for visual inspection and to assess convergence. It helps to quickly view median estimates and 95% highest posterior density intervals of the parameters, and calculates the effective sample sizes (ESS) of parameters. It can also be used to investigate potential parameter correlations. We will be using Tracer v1.7.0.

----

# Practical: Tree Set Analysis

We will set up an analysis in BEAUti using a fixed tree partition. We add a spherical geography partition, run BEAST and analyse the results. We will be using the FixedTreeAnalysis and GEO_SPHERE packages.

> * Start BEAUti
> * Click to the `File => Manage packages` menu item.
> * Select `FixedTreeAnalysis` in the list of packages and the click `Install` button.
> * Select `GEO_SPHERE` and the click `Install` button.
> * Close BEAUti -- it needs to restart to pick up the new packages.


## Set up in BEAUti

> Start BEAUti and select the `File => Templates => Tree Set Analysis` item

BEAUti should change to show it uses the Tree Set Analysis template.

<figure>
	<a id="fig:BEAUti1"></a>
	<img style="width:45%;" src="figures/BEAUti-fixed-tree.png" alt="">
	<img style="width:45%;" src="figures/BEAUti-partition0.png" alt="">
	<figcaption>Figure 1: Select the Tree Set Analysis template, and BEAUti changes its appearance.</figcaption>
</figure>


> Next, select the `File => Add Tree Set` menu.
A dialog is shown where you can select a file containing a tree in NEXUS format.

> Select the file `H5N1.tree` that comes with this tutorial in the data section.

<figure>
	<a id="fig:BEAUti2"></a>
	<img style="width:45%;" src="figures/BEAUti-import.png" alt="">
	<img style="width:45%;" src="figures/BEAUti-partitions1.png" alt="">
	<figcaption>Figure 2: Add Fixed Tree partition through the `File => Import Fixed Tree` menu.</figcaption>
</figure>

In the partition panel, a new partition will be added with the name H5N1. The site model and clock model panels will show entries for this partition that cannot be changed. The priors panel contains a dummy tree distribution, that will remain constant throughout the analysis. If you want to set up an analysis with an epidemiological tree prior, you can choose another tree prior and estimate parameters for this tree. Here, we will add another partition for a discrete trait.

> Select the `File => Add Discrete Trait` menu.

<figure>
	<img style="width:45%;" src="figures/BEAUti-discrete-trait.png" alt="">
</figure>

A dialog is shown to set up the name. Choose `geo`. There is only a single tree in the system, so no need to change it. When adding other kinds of partitions, it may be necessary to select the fixed tree, or after adding the partition, to select all partitions and use the `Link Trees` button.

<figure>
	<img style="width:45%;" src="figures/BEAUti-create-trait.png" alt="">
</figure>

Next, a dialog is shown where you can set up the trait. Since there are quite a few taxa, we will just load them from the `H5N1locations.dat` file in the data directory.

> * Choose the `Guess` button.
> * Select `read from file`
> * Select the `Browse` button, and navigate to the `H5N1locations.dat` file in the data section of this tutorial.
> * Select the `OK` button.
> * All location entries are now populated. Select the `Close` button.

<figure>
	<a id="fig:BEAUti3"></a>
	<img style="width:45%;" src="figures/BEAUti-dta-set-locations.png" alt="">
	<img style="width:45%;" src="figures/BEAUti-dta-set-locations2.png" alt="">
	<img style="width:45%;" src="figures/BEAUti-partition2.png" alt="">
	<figcaption>Figure 3: Set up DTA locations.</figcaption>
</figure>

A second partition should now have been added to the partitions panel. If necessary, the site model and its parameters can be changed in the site model panel, and the clock model in the clock model panel for the DTA partition. However, for this tutorial, we will keep things simple and use the default site model, clock model and priors.


Since the analysis will converge quite quickly, we do not need the default 10 million samples.

> * In the MCMC panel, set the chainLength to 1 million samples.
> * Change to log frequency of the `treeWithTraitLog` to 1000.
> * Optionally, you might want to reduce the log frequency of the screen logger to 100000.
> * Safe the file to `H5N1-dta-fixed-tree.xml`

<figure>
	<a id="fig:BEAUti4"></a>
	<img style="width:45%;" src="figures/BEAUti-mcmc.png" alt="">
	<figcaption>Figure 4: MCMC settings.</figcaption>
</figure>


## Run with BEAST

> Run BEAST on `HBVtreeset-geo.xml`

This should not take too long.

## Check convergence

> Run `Tracer`, and make sure all parameters have sufficiently large ESSs

<figure>
	<a id="fig:Tracer"></a>
	<img style="width:65%;" src="figures/tracer1.png" alt="">
	<figcaption>Figure 5: Convergence of MCMC in Tracer.</figcaption>
</figure>


## Comparison with joint analysis


```
applauncher HeatMap -tree HBVtreeset-geo.trees -tag locationsgeo -back ~/data/map/World98b.png -bound -30,-30,70,160 -width 1024 -height 512
```

----

# Useful Links

- BEAST 2 website and documentation: [http://www.beast2.org/](http://www.beast2.org/)
- [Bayesian Evolutionary Analysis with BEAST 2](http://www.beast2.org/book.html) {% cite BEAST2book2014 --file master-refs.bib %}
- Join the BEAST user discussion: [http://groups.google.com/group/beast-users](http://groups.google.com/group/beast-users)

----

# Relevant References

{% bibliography --cited --file master-refs.bib %}
