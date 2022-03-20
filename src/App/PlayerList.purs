module App.PlayerList
  ( State
  , playerFormComponent
  )
  where

import Prelude

import CSS (display, flex)
import CSS.Flexbox (justifyContent, spaceBetween)
import DOM.HTML.Indexed.InputType (InputType(..))
import Data.Array (snoc)
import Data.Const (Const)
import Data.Either (Either(..))
import Data.Maybe (Maybe(..))
import Data.Newtype (class Newtype)
import Data.String (length)
import Effect.Aff (Aff)
import Effect.Class.Console (logShow)
import Formless (Event(..), FormField(..), OutputField(..), Validation)
import Formless as F
import Formless.Validation (hoistFnE_)
import Halogen as H
import Halogen.HTML as HH
import Halogen.HTML.CSS as HC
import Halogen.HTML.Events as HE
import Halogen.HTML.Properties as HP
import Type.Prelude (Proxy(..))

-- track the names of all the players who are participating
type State =
  { confirmedPlayers :: Array String,
    pendingPlayer :: String
  }

newtype PlayersEntryForm (r :: Row Type -> Type) f = PlayersEntryForm
  ( r
      (playerName :: f Boolean String String)
  )

derive instance Newtype (PlayersEntryForm r f) _

data FormAction =
  ChangePlayerName String
  | SubmitPlayerName

type FieldConfig :: Symbol -> Type
type FieldConfig sym =
  { label :: String
  , help :: String
  , placeholder :: String
  , sym :: Proxy sym
  }

minLength :: forall form m. Monad m => Int -> Validation form m Boolean String String
minLength n = hoistFnE_ $ \str ->
  let
    n' = length str
  in
    if n' < n then Left false else Right str

playerFormComponent :: H.Component (Const Void) Unit Void Aff
playerFormComponent = H.mkComponent
  { initialState: const { pendingPlayer: "", confirmedPlayers: []}
  , render: render'
  , eval: H.mkEval $ H.defaultEval { handleAction = handleAction }
  }
  where
  handleAction = case _ of
    ChangePlayerName s -> logShow s *> (H.modify_ $ \st -> st { pendingPlayer = s }) *> (H.get >>= logShow)
    SubmitPlayerName -> do
      st@{ confirmedPlayers, pendingPlayer } <- H.get
      H.put { confirmedPlayers: confirmedPlayers `snoc` pendingPlayer, pendingPlayer: "" }
      logShow st
  render' { confirmedPlayers } =
    HH.div [ HC.style $ justifyContent spaceBetween ] $
      [ HH.slot F._formless unit formComponent unit ChangePlayerName
      , HH.button [ HE.onClick (const SubmitPlayerName) ] [ HH.text "submit" ]
      , HH.div_ $ HH.text <$> confirmedPlayers ]

  -- TODO: need to figure out how to raise the result here to the parent component
  -- I _think_ I have to do this with queries, but I don't know how to do that yet,
  -- but fortunately it's the next section of the book
  formComponent :: F.Component PlayersEntryForm (Const Void) () Unit String Aff
  formComponent = F.component (const formInput) $ F.defaultSpec
    { render = renderFormless
    , handleEvent = F.raiseResult
    }
    where
    formInput =
      { validators: PlayersEntryForm { playerName: minLength 5 }
      , initialInputs: Nothing
      }
    renderFormless st =
      formContent_
        [ input
            { label: "Player's name"
            , help: pure "Write the name of the next player"
            , placeholder: "James"
            }
            [ HP.value $ F.getInput _name st.form
            , HE.onValueInput (F.setValidate _name)
            ]
        ]
      where
      _name = Proxy :: Proxy "playerName"

    formContent_ content =
      HH.div
        [ class_ "content", HC.style $ display flex ]
        [ HH.div
            [ class_ "column has-background-white-bis" ]
            content
        ]
    input config props =
      field
        { label: config.label, help: config.help }
        [ HH.input $
            [ HP.type_ InputText
            , HP.placeholder config.placeholder
            ] <> props
        ]
    class_ = HP.class_ <<< HH.ClassName
    field config contents =
      HH.div
        [ class_ "field" ]
        [ HH.div
            [ class_ "label" ]
            [ HH.text config.label ]
        , HH.div
            [ class_ "control" ]
            contents
        , case config.help of
            Left str -> helpError_ str
            Right str -> help_ str
        ]
      where
      help_ str = HH.p [ class_ "help" ] [ HH.text str ]
      helpError_ str = HH.p [ class_ "help is-danger" ] [ HH.text str ]