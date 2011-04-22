;  Copyright (c) Dave Ray, 2011. All rights reserved.

;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this 
;   distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns seesaw.examples.kitchensink
  (:require clojure.java.io)
  (:use seesaw.core)
  (:use seesaw.border)
  (:import (javax.swing JFrame JLabel)
           (java.awt Color)))

(def rss-url (clojure.java.io/resource "seesaw/examples/rss.gif"))
(def redditor "http://static.reddit.com/reddit.com.header.png")

(defn app []
  (frame :title "Hello Seesaw" :width 600 :height 600 :pack? false :content
    (border-panel :vgap 5
      :north (toolbar 
               :floatable? false 
               :items [(button :id :button :text "This") :separator "is a toolbar" :separator
                       (action :handler #(.dispose (to-frame %)) :name "Close this frame")
                       (combobox :id :combo :model ["First" "Second" "Third"])])
      :center (top-bottom-split 
      (left-right-split 
        (border-panel 
          :hgap 12 :vgap 15
          :background Color/ORANGE
          :border [10 "This is a border layout" (empty-border :thickness 15)]
          :north (horizontal-panel 
                  :items [(action 
                            :handler #(println "FOO" %) 
                            :name "Click Me"
                            :icon rss-url
                            :tip "Yum!")
                          "<html>Multi-<br><b>LINE</b></html>"
                          :fill-h
                          (toggle 
                            :id :and-me
                            :text "And Me"
                            :icon redditor
                            :tip "Yum!")])
          :center (vertical-panel 
                    :items [(label 
                              :border (line-border) 
                              :text "This label acts like a link" 
                              :id :link)
                            (text 
                              :text "HI"
                              :listen [:action :handler (fn [e] (println (.. (to-widget e) (getText))))])
                            (scrollable 
                              (text 
                                :text (apply str (interpose "\n" (range 0 20))) 
                                :multi-line? true 
                                :editable? false))])
          :east  (JLabel. "East")
          :west  (vertical-panel 
                    :background Color/GREEN
                    :border (line-border :color Color/YELLOW :thickness 5) 
                    :items ["A" :fill-v rss-url "C" [:fill-v 45] "D"])
          :south (horizontal-panel 
                  :border [(line-border :top 5) (line-border :top 10 :color "#FF0000")]
                  :items ["A" 
                          :fill-h 
                          "B" 
                          [:fill-h 20] 
                          rss-url 
                          "C"
                          (checkbox :id :check-me, :text "Check me")
                          ]))
      (grid-panel 
        :border [10 "Here's a grid layout with 3 columns" 10]
        :hgap 10 
        :vgap 10 
        :columns 3 
        :items (map #(action 
                      :handler (fn [e] (alert (str "Clicked " %))) 
                      :name %) 
                    (range 0 12))))
  (tabbed-panel 
    :id :tabs
    :placement :bottom
    :tabs [
      { :title "flow-panel"
        :tip   "Example of a flow-panel"
        :content
          (flow-panel
            :align :right
            :border "Here's a right-aligned flow layout"
            :items (map #(label :opaque? true :background "#ccccff" :text %) (range 10000 10030)))}
      { :title (horizontal-panel 
                 :opaque? false 
                 :items ["This tab has a button -> " (button :text "X")])
        :tip   "Here's another tab"
        :content "Hello. I'm the content of this tab. Just a label." }
      { :title "JList Example"
        :tip   "A tab with a JList example"
        :content (scrollable (listbox :id :list :model (range 0 100))) }]))))

  (listen (select :#tabs) :state-changed
        #(let [tp (to-widget %)
               tab (.getSelectedIndex tp)] 
          (.setTitleAt tp 0 (if (= tab 0) ":)" ":("))))

  (listen (select :#button) :action (fn [e] (alert "HI")))

  (listen (select :#link)
    :mouse-clicked #(alert % "CLICK!")
    :mouse-entered #(config % :foreground Color/BLUE)
    :mouse-exited  #(config % :foreground Color/BLACK))

  (listen (select :#check-me) :selection ; or :item-state-changed
    (fn [e] 
      (config (select :#link) :enabled? (first (selection e)))))

  (listen (select :#combo)  :selection ; or :item-state-changed
      (fn [e] (println (selection e))))

  (listen (select :#list)  :selection ; or :list-selection
      (fn [e] (println (selection e))))

  (listen (select :#and-me)  :selection ; or :item-state-changed
    (fn [e] (println (selection e)))))

 

(defn -main [& args]
  (invoke-later app))
;(doseq [f (JFrame/getFrames)]
  ;(.dispose f))
;(-main)
