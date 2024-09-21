(ns core.context)

(defrecord Context [])

(defprotocol EffectHandler
  (do! [_ txs])
  (summarize-txs [_ txs])
  (frame->txs [_ frame-number]))

(defprotocol MouseOverEntity
  (update-mouseover-entity [_])
  (mouseover-entity* [_]))

(defprotocol WorldRaycaster
  (ray-blocked?  [_ start target])
  (path-blocked? [_ start target path-w] "path-w in tiles. casts two rays."))

(defprotocol WorldLineOfSight
  (line-of-sight? [_ source* target*]))

(defprotocol ExploredTileCorners
  (explored? [_ position]))

(defprotocol World
  (active-entities [_])
  (world-grid [_]))

(defprotocol BackgroundImage
  (->background-image [_]))

; skills & effects together = 'core.action' ?
(defprotocol ActiveSkill
  (skill-usable-state [ctx entity* skill]))

; core.property.types.world ?
(defprotocol WorldGenerator
  (->world [ctx world-id]))

(defprotocol ErrorWindow
  (error-window! [_ throwable]))

(defprotocol PropertyEditor
  (->overview-table [_ property-type clicked-id-fn]
   "Creates a table with all-properties of property-type and buttons for each id which on-clicked calls clicked-id-fn."))
