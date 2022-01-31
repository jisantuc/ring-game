with import <nixpkgs> { };

{ pkgs ? import <nixpkgs> { } }:
pkgs.mkShell {
  name = "ring-game";
  buildInputs = [ purescript spago nodejs-17_x ];
}
