# fireplace.vim and boot build tool

This repository is intended for experimentation with [fireplace.vim](https://github.com/tpope/vim-fireplace)
in combination with the [boot](https://github.com/boot-clj/boot) build tool; specifically the ClojuresScript
workflow implemented by [adzerk](https://github.com/adzerk/boot-cljs-repl).

A nice introduction to boot and the ClojureScript workflow can be found on [the adzerk blog](http://adzerk.com/blog/2014/11/clojurescript-builds-rebooted/).

## Usage

To get up and running, follow these steps:

1. In a terminal, run `boot watch cljs-repl cljs -usO none reload` to start a 
2. In a browser, open `target/index.html`
2. `vim build.boot`
3. `:set ft=clojure`
4. `:Connect` with the nREPL port from step 1
5. `cqp` and invoke `(require '[adzerk.boot-cljs-repl :refer :all])`
6. `cqp` and invoke `(start-repl)`

You should now have a browser-connected REPL from the `user` namespace, and
the browser console should say "Opened Websocket REPL connection".

If you try switching to a different namespace, e.g. `fireplace-boot-example`,
you should see that the browser REPL is not available there.

## License

Copyright © 2014 Matthias Diehn Ingesman

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
