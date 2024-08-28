(ns app
  (:require [clojure.edn :as edn]
            [gdx.app :refer [->application-listener]]
            [gdx.backends.lwjgl3 :as lwjgl3]
            [gdx.graphics.color :as color]
            [gdx.utils.screen-utils :as screen-utils]
            [core.component :as component]
            [core.context :as ctx]))

(def state (atom nil))

(defn- create-context [components]
  (component/require-all!)
  (->> components
       (component/create-into (assoc (ctx/->Context) :context/state state))
       ctx/init-first-screen
       (reset! state)))

(defn- destroy-context []
  (run! component/destroy @state))

(defn- render-context []
  (ctx/fix-viewport-update @state)
  (screen-utils/clear color/black)
  (-> @state
      ctx/current-screen
      (component/render! state)))

(defn- update-viewports [w h]
  (ctx/update-viewports @state w h))

(defn- ->application [components]
  (->application-listener
   :create #(create-context components)
   :dispose destroy-context
   :render render-context
   :resize update-viewports))

(defn -main [& [app-edn-file]]
  (let [app (-> app-edn-file slurp edn/read-string)]
    (assert (:context app))
    (assert (:app app))
    (lwjgl3/->application (->application (:context app))
                          (lwjgl3/->configuration (:app app)))))
