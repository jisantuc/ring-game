module App.PlayerList where

import Prelude

import Data.UUID (UUID)
import Halogen as H
import Halogen.HTML as HH
-- import Halogen.HTML.Events as HE

-- track the names of all the players who are participating
type Player = { id :: UUID, name :: String }
type State = Array Player

data Action
  = AddPlayer
  | RemovePlayer Int
  | StartGame

renderPlayerList :: forall cs m. State -> H.ComponentHTML Action cs m
renderPlayerList players = case players of
  [] -> HH.input [ ]
  ps -> HH.div_ $ (\{name} -> HH.text name) <$> ps

render :: forall cs m. State -> H.ComponentHTML Action cs m
render state =
  HH.div_
    [ HH.p_
        [ HH.text $ "Who's playing?" ]
    , renderPlayerList state
    ]

-- handleAction :: forall cs o m. Action → H.HalogenM State Action cs o m Unit
-- handleAction action = ???

component :: forall q i o m. H.Component q i o m
component =
  H.mkComponent
    { initialState: \_ -> []
    , render
    , eval: H.mkEval H.defaultEval
    }