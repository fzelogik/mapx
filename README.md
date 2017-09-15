# mapx

[![Clojars Project](https://img.shields.io/clojars/v/chrisjd/mapx.svg)](https://clojars.org/chrisjd/mapx)
[![Build Status](https://travis-ci.org/chrisjd-uk/mapx.svg?branch=master)](https://travis-ci.org/chrisjd-uk/mapx)

A very simple library for performing powerful map transformations in
Clojure.

The problem that this library solves is needing to transform maps from
one domain (e.g. results fetched from a database) into another domain
(e.g. your application's data model).

This process generally requires 3 steps:

- Select, delete or insert certain keys from the input map.
- Apply transformations to values in the map.
- Rename keys.

mapx provides just one core function to achieve this in a declarative,
clear manner: `mapx.core/map-xform`.


## Example

For the examples below:

```clojure
user> (require '[mapx.core :as mx])
nil
```

### Inserting Missing Keys

``` clojure
user> (mx/map-xform {:a 1 :b 2 :c 3} :or {:a 123 :d 4})
{:a 1, :b 2, :c 3, :d 4}
```

### Selecting Keys

```clojure
user> (mx/map-xform {:a 1 :b 2 :c 3} :select [:a :c])
{:a 1, :c 3}
```

### Deleting Keys

```clojure
user> (mx/map-xform {:a 1 :b 2 :c 3} :delete [:a])
{:b 2, :c 3}
```

### Updating Values

```clojure
user> (mx/map-xform {:a 1 :b 2 :c 3} :update {:a inc, :c (partial * 20)})
{:a 2, :b 2, :c 60}
```

### Renaming Keys

```clojure
user> (mx/map-xform {:a 1 :b 2 :c 3} :rename {:a :x, :b :y, :c :z})
{:x 1, :y 2, :z 3}
```

### All Together

```clojure
user> (mx/map-xform {:a 1 :b 2 :c 3}
                    :select [:a :c]
                    :delete [:c]
                    :update {:a str}
                    :rename {:a :foo})
{:foo "1"}
```

### Projection

``` clojure
user> (mx/map-xform {:a 1 :b 2 :c 3}
                    :project {:a :x :c :z})
{:x 1, :z 3}
```


## License

Copyright © 2017 Chris J-D

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
