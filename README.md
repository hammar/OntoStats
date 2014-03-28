OntoMetrics
===========

<karl@karlhammar.com>

A pluggable tool for measuring ontology metrics.

Building
----------
Building the OntoMetrics tool requires the use of Maven.

```sh
$ cd ontometrics
$ mvn -X package
```

Unfortunatly we need to pass in the location of some /diagrammetrics/ related resources.

Running
----------
A run target is defined for the Maven exec:java lifecycle target.

```sh
$ mvn exec:java
```

Development Environment
----------
To hack on OntoStats using Eclipse as your IDE, it is easiest to:

```sh
$ mvn eclipse:eclipse
```

and then import it as an existing Eclipse project.