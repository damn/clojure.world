(ns components.properties.skill
  (:require [clojure.string :as str]
            [utils.core :refer [readable-number]]
            [core.component :refer [defcomponent] :as component]
            [core.data :as data]
            [core.context :as ctx]))

(def ^:private skill-cost-color "[CYAN]")
(def ^:private action-time-color "[GOLD]")
(def ^:private cooldown-color "[SKY]")
(def ^:private effect-color "[CHARTREUSE]")

(defcomponent :properties/skill
  (component/create [_ _ctx]
    (defcomponent :skill/action-time {:data data/pos-attr})
    (defcomponent :skill/cooldown {:data data/nat-int-attr})
    (defcomponent :skill/cost {:data data/nat-int-attr})
    (defcomponent :skill/effects {:data (data/components-attribute :effect)})
    (defcomponent :skill/start-action-sound {:data data/sound})
    (defcomponent :skill/action-time-modifier-key {:data (data/enum :stats/cast-speed :stats/attack-speed)})
    {:id-namespace "skills"
     :schema (data/map-attribute-schema
              [:property/id [:qualified-keyword {:namespace :skills}]]
              [:property/image
               :skill/action-time
               :skill/cooldown
               :skill/cost
               :skill/effects
               :skill/start-action-sound
               :skill/action-time-modifier-key])
     :edn-file-sort-order 0
     :overview {:title "Skills"
                :columns 16
                :image/dimensions [70 70]}
     :->text (fn [ctx {:keys [property/id
                              skill/action-time
                              skill/cooldown
                              skill/cost
                              skill/effects
                              skill/action-time-modifier-key]}]
               [(str/capitalize (name id))
                (str skill-cost-color "Cost: " cost "[]")
                (str action-time-color
                     (case action-time-modifier-key
                       :stats/cast-speed "Casting-Time"
                       :stats/attack-speed "Attack-Time")
                     ": "
                     (readable-number action-time) " seconds" "[]")
                (str cooldown-color "Cooldown: " (readable-number cooldown) "[]")
                ; don't used player-entity* as it may be nil when just created, could use the current property creature @ editor
                (str effect-color
                     (ctx/effect-text (assoc ctx :effect/source (ctx/player-entity ctx))
                                      effects)
                     "[]")])}))
