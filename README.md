# TinySegmenter

TinySegmenter is a Clojure library which splits Japanese into words.
This is needed because the Japanese stubbornly refuse to use the spacebar.

This library is just a Clojure port of the TinySegmenter javascript library by Taku Kudo (taku@chasen.org).
This version is based on the [Python 3 version of TinySegmenter](https://github.com/SamuraiT/tinysegmenter/blob/master/tinysegmenter/tinysegmenter.py)

## Usage

The library only exports a single function `segment`, which takes a string (or any char sequence) as an argument.

``` clojure
(= (segment "私の名前はFelixです")
   ["私" "の" "名前" "は" "Felix" "です"])
```

## License

This project is distributed under the BSD 3 License, just like [the original version by Taku Kudo](http://chasen.org/~taku/software/TinySegmenter/LICENCE.txt).
See the LICENSE file for more information.
