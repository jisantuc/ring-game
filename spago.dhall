{ name = "halogen-project"
, dependencies =
  [ "aff"
  , "console"
  , "const"
  , "css"
  , "dom-indexed"
  , "effect"
  , "either"
  , "halogen"
  , "halogen-css"
  , "halogen-formless"
  , "maybe"
  , "newtype"
  , "prelude"
  , "psci-support"
  , "strings"
  , "typelevel-prelude"
  , "uuid"
  ]
, packages = ./packages.dhall
, sources = [ "src/**/*.purs", "test/**/*.purs" ]
}
