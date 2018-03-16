(ns status-im.utils.mixpanel
  (:require-macros [status-im.utils.slurp :as slurp])
  (:require [cljs.reader :as reader]
            [goog.crypt.base64 :as b64]
            [status-im.utils.datetime :as datetime]
            [status-im.utils.http :as http]
            [status-im.utils.types :as types]))

(def token "3f2e1a8970f159aa2a3d5dc5d65eab38")

(def base-url "http://api.mixpanel.com/")
(def base-track-url (str base-url "track/"))

(defn encode [m]
  (b64/encodeString (types/clj->json m)))

(defn- build-url [id label props]
  (str base-track-url
       "?data="
       (encode {:event      label
                :properties (merge
                              {:token       token
                               :distinct_id id
                               :time        (datetime/timestamp)}
                              props)})))

(defn track
  ([id label] (track id label nil))
  ([id label props]
   (http/get (build-url id label props))))

(def events (reader/read-string (slurp/slurp "./src/status_im/utils/mixpanel_events.edn")))
(def event-by-trigger (reduce-kv #(assoc %1 (first (:trigger %3)) %3) {} events))

(defn matching-event [[kind _]]
  (kind event-by-trigger))


