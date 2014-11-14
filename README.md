# UPDATE:

This repo now demonstrates a boot task to use with fireplace.vim. See usage.

# fireplace.vim and boot build tool

Issue that gave rise to this repository: [fireplace.vim issue #185](https://github.com/tpope/vim-fireplace/issues/185).

This repository is intended for experimentation with [fireplace.vim](https://github.com/tpope/vim-fireplace)
in combination with the [boot](https://github.com/boot-clj/boot) build tool; specifically the ClojuresScript
workflow implemented by [adzerk](https://github.com/adzerk/boot-cljs-repl).

A nice introduction to boot and the ClojureScript workflow can be found on [the adzerk blog](http://adzerk.com/blog/2014/11/clojurescript-builds-rebooted/).

## Usage

To get up and running, follow these steps:

1. In a terminal, run `boot watch cljs-repl cljs -usO none reload` to start a 
2. In a browser, open `target/index.html`
3. Open any of the files in `src/cljs` with vim
4. `:Connect` with the nREPL port from step 1
5. `:Piggieback (boot.user/repl-env)`

You should now have a browser-connected REPL and the browser console should say "Opened Websocket REPL connection".

## License

Copyright © 2014 Matthias Diehn Ingesman

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
