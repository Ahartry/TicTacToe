#!/bin/bash
script_dir=$(dirname "$0")
"$script_dir"/jre/bin/java --enable-preview -jar "$script_dir"/TicTacToe.jar
