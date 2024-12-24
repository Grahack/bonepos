(ns ^:figwheel-hooks bonepos.core
  (:require
   [goog.dom :as gdom]
   [reagent.core :as reagent :refer [atom]]))

(defonce app-state (atom {:text "Hello world!"}))

(defn get-app-element []
  (gdom/getElement "app"))

(def W 30)
(def H 200)
(def slide-h 150)
(def slide-sep 8)
(def note-r (- slide-sep 1))
(def offset-l 1) ; left, just for the notes
(def offset-t (+ 1 note-r)) ; top
(def offset-label (+ slide-h 35))
(def coef-p1 0)
(def coef-p2 0.12)
(def coef-p3 0.26)
(def coef-p4 0.42)
(def coef-p5 0.59)
(def coef-p6 0.78)
(def coef-p7 1)
(def p1 slide-h)
(def p2 (* (- 1 coef-p2) slide-h))
(def p3 (* (- 1 coef-p3) slide-h))
(def p4 (* (- 1 coef-p4) slide-h))
(def p5 (* (- 1 coef-p5) slide-h))
(def p6 (* (- 1 coef-p6) slide-h))
(def p7 0)

(defn pos [p]
  (nth [p1 p2 p3 p4 p5 p6 p7] (- p 1)))

(defn harm [h]
  ; original parenedit colors "#fab" "#fdb" "#ffd" "#dfb" "#cef" "#dcf" "#bbe"
  (nth ["#f99" "#fdb" "#ffd" "#dfb" "#cef" "#dcf" "#a8c" "#75a" "#339" "#000"]
       (- h 1)))

(defn rect [h]
  [:svg {:width 25 :height 25}
    [:rect {:x 0 :y 0 :width 25 :height 25 :fill (harm h) :stroke "black"}]])

(defn line
  ([x1 y1 x2 y2]
   (line x1 y1 x2 y2 1 "black"))
  ([x1 y1 x2 y2 w color]
   [:line {:x1 x1 :y1 y1 :x2 x2 :y2 y2 :stroke-width w :stroke color}]))

(defn empty-slide []
  (let [x (+ 1 slide-sep) ; add 1 to not crop notes
        y offset-t]
      [:g (line x y x (+ y slide-h))
          (line (- x slide-sep) (+ y p6) (+ x slide-sep) (+ y p6))
          (line (- x slide-sep) (+ y p4) (+ x slide-sep) (+ y p4))
          (line (- x slide-sep) (+ y p2) (+ x slide-sep) (+ y p2))]))

(defn empty-slide2 []
  (let [x (+ 1 slide-sep) ; add 1 to not crop notes
        y offset-t]
      [:g (line x y x (+ y slide-h))
          (line (- x slide-sep) (+ y p5) (+ x slide-sep) (+ y p5))
          (line (- x slide-sep) (+ y p3) (+ x slide-sep) (+ y p3))]))

(defn add-pos-numbers [slide]
  [:svg {:width (+ W 20) :height H}
    [:g slide
        (map #(identity [:text {:x 22 :y (+ 6 offset-t (pos %))
                                :font-size 20} (str %)])
             (range 1 8))]])

(defn sep []
  (let [x (+ 1 slide-sep) ; add 1 like (slide)
        y offset-t]
     [:svg {:width W :height H} [:g (line x y x (+ y slide-h))]]))

(defn note-dot [N] ; N is [pos harm]
    (let [p (first N)
          h (second N)]
      [:circle {:cx (+ offset-l slide-sep) :cy (+ offset-t (pos p))
                :r note-r
                :fill (harm h)}]))

(defn bold-dot [p]
      [:circle {:cx (+ offset-l slide-sep) :cy (+ offset-t (pos p))
                :r note-r
                :stroke-width 3 :fill "none"}])

(defn slide-n-notes [label notes]
  [:g (empty-slide2)
      (map #(identity (note-dot %)) (partition 2 notes))
      [:text {:x 0 :y offset-label :font-size 20} label]])

(defn diag
  ([notes] (diag "" notes))  ; notes is like [p1 h1  p2 h2  p3 h3...]
  ([label notes]
     [:svg {:width W :height H}
       (slide-n-notes label notes)
       (bold-dot (first notes))]))

; ♭ ♮ ♯ ø
(def E1  (diag "E"  [7 1]))
(def Es1 (diag "E♯" [6 1]))
(def F1  (diag "F"  [6 1]))
(def Fs1 (diag "F♯" [5 1]))
(def Gf1 (diag "G♭" [5 1]))
(def G1  (diag "G"  [4 1]))
(def Gs1 (diag "G♯" [3 1]))
(def Af1 (diag "A♭" [3 1]))
(def A1  (diag "A"  [2 1]))
(def As1 (diag "A♯" [1 1]))
(def Bf1 (diag "B♭" [1 1]))
(def B1  (diag "B"  [7 2]))
(def Cf2 (diag "C♭" [7 2]))
(def C2  (diag "C"  [6 2]))
(def Cs2 (diag "C♯" [5 2]))
(def Df2 (diag "D♭" [5 2]))
(def D2  (diag "D"  [4 2]))
(def Ds2 (diag "D♯" [3 2]))
(def Ef2 (diag "E♭" [3 2]))
(def E2  (diag "E"  [2 2  7 3]))
(def Es2 (diag "E♯" [1 2  6 3]))
(def F2  (diag "F"  [1 2  6 3]))
(def Fs2 (diag "F♯" [5 3]))
(def Gf2 (diag "G♭" [5 3]))
(def G2  (diag "G"  [4 3]))
(def Gs2 (diag "G♯" [3 3  7 4]))
(def Af2 (diag "A♭" [3 3  7 4]))
(def A2  (diag "A"  [2 3  6 4]))
(def As2 (diag "A♯" [1 3  5 4]))
(def Bf2 (diag "B♭" [1 3  5 4]))
(def B2  (diag "B"  [4 4  7 5]))
(def Bs2 (diag "B♯" [3 4  6 5]))
(def Cf3 (diag "C♭" [4 4  7 5]))
(def C3  (diag "C"  [3 4  6 5]))
(def Cs3 (diag "C♯" [2 4  5 5]))
(def Df3 (diag "D♭" [2 4  5 5]))
(def D3  (diag "D"  [1 4  4 5  7 6]))
(def Ds3 (diag "D♯" [3 5  6 6]))
(def Ef3 (diag "E♭" [3 5  6 6]))
(def E3  (diag "E"  [2 5  5 6  7 7]))
(def Es3 (diag "E♯" [1 5  4 6  6 7]))
(def F3  (diag "F"  [1 5  4 6  6 7]))
(def Fs3 (diag "F♯" [3 6  5 7  7 8]))
(def Gf3 (diag "G♭" [3 6  5 7  7 8]))
(def G3  (diag "G"  [2 6  4 7  6 8]))
(def Gs3 (diag "G♯" [1 6  3 7  5 8  7 9]))
(def Af3 (diag "A♭" [1 6  3 7  5 8  7 9]))
(def A3  (diag "A"  [2 7  4 8  6 9]))
(def As3 (diag "A♯" [1 7  3 8  5 9  7 10]))
(def Bf3 (diag "B♭" [1 7  3 8  5 9  7 10]))

(defn extract [diagrams]
  ; extract the relevant SVG group from a 'diag' vector
  ; and store them in another vector
  (map #(nth % 2) diagrams))

(defn translate [Tx svg-group]
  [:g {:transform (str "translate(" (* W Tx) " 0)")} svg-group])

(defn trick [data]
  (let [part (partition 2 data)
        wrapped-notes (map first part)
        positions (map second part)
        bold-dots (map bold-dot positions)
        n (count wrapped-notes)
        trick-W (* W n)
        notes (extract wrapped-notes)]
    [:svg {:width trick-W :height H}
      (map translate (range n) notes)
      (map translate (range n) bold-dots)]))

(def topo-r 5)
(def W-topo 250)
(def W-topo2 (+ W-topo topo-r))
(def H-topo 600)
(def H-topo2 (- H-topo topo-r))
(def step 25)

(defn range1 [n]
  (range 1 (+ 1 n)))

(defn hp-to-point [[h p]]
  (let [ps (* p step)]
    (case h
      7 [(- W-topo2 ps) (- (+ H-topo2 ps) 614)]  ; highest partial
      6 [(- W-topo2 ps) (- (+ H-topo2 ps) 575)]
      5 [(- W-topo2 ps) (- (+ H-topo2 ps) 520)]
      4 [(- W-topo2 ps) (- (+ H-topo2 ps) 465)]
      3 [(- W-topo2 ps) (- (+ H-topo2 ps) 385)]
      2 [(- W-topo2 ps) (- (+ H-topo2 ps) 300)]
      1 [(- W-topo2 ps) (- (+ H-topo2 ps) 200)])))

(defn point [[x y]]
  [:circle {:cx x :cy y :r topo-r :fill "black"}])

(defn p-color [n]
  ; original parenedit colors "#fab" "#fdb" "#ffd" "#dfb" "#cef" "#dcf" "#bbe"
  (nth ["#f33" "#fb9" "#eea" "#cec" "#bdf" "#dcf" "#a8d"]
       (- 7 n)))

(defn trajet-l
  ([h1 p1 h2 p2 color]
   (trajet-l h1 p1 h2 p2 color 0))
  ([h1 p1 h2 p2 color offset]
  (let [[x1 y1] (hp-to-point [h1 p1])
        [x2 y2] (hp-to-point [h2 p2])]
     (line (+ x1 offset) y1 (+ x2 offset) y2 3 color))))

(defn trajet-c [h1 p1 h2 p2 cx1 cy1 cx2 cy2 color]
  (let [[x1 y1] (hp-to-point [h1 p1]) [x2 y2] (hp-to-point [h2 p2])]
    [:path {:stroke-width 3 :stroke color :fill "none"
            :d (str "M " x1 " " y1 " "    ; move to origin
                    "C " (+ x1 cx1) " "   ; curve with control 1 (x)
                         (+ y1 cy1) ", "  ;                      (y)
                         (+ x2 cx2) " "   ; control 2 (x)
                         (+ y2 cy2) ", "  ;           (y)
                    x2 " " y2)}]))        ; destination (absolute positions)

(defn hello-world []
  [:div
   [:h1 "Schémas pour quelques gammes au trombone"]
   [:h2 "Table des matières"]
   [:ul
     [:li [:a {:href "#comment"} "Fonctionnement des schémas"]]
     [:li [:a {:href "#chro"} "Gammes chromatiques"] " ("
          [:a {:href "#chro-asc"} "asc"] " - "
          [:a {:href "#chro-desc"} "desc"] ")"]
     [:li [:a {:href "#une-pos"} "Notes par nombre de positions"]]
     [:li "Gammes majeures : "
         [:a {:href "#M-C"}  "C"]  "-"
         [:a {:href "#M-Df"} "D♭"] "-"
         [:a {:href "#M-D"}  "D"]  "-"
         [:a {:href "#M-Ef"} "E♭"] "-"
         [:a {:href "#M-E"}  "E"]  "-"
         [:a {:href "#M-F"}  "F"]  "-"
         [:a {:href "#M-Fs"} "F♯"] "-"
         [:a {:href "#M-Gf"} "G♭"] "-"
         [:a {:href "#M-G"}  "G"]  "-"
         [:a {:href "#M-Af"} "A♭"] "-"
         [:a {:href "#M-A"}  "A"]  "-"
         [:a {:href "#M-Bf"} "B♭"] "-"
         [:a {:href "#M-B"}  "B"]
     ]
     [:li "Pentas majeures : "
         [:a {:href "#P-C"}  "C"]  "-"
         [:a {:href "#P-Df"} "D♭"] "-"
         [:a {:href "#P-D"}  "D"]  "-"
         [:a {:href "#P-Ef"} "E♭"] "-"
         [:a {:href "#P-E"}  "E"]  "-"
         [:a {:href "#P-F"}  "F"]  "-"
         [:a {:href "#P-Fs"} "F♯"] "-"
         [:a {:href "#P-Gf"} "G♭"] "-"
         [:a {:href "#P-G"}  "G"]  "-"
         [:a {:href "#P-Af"} "A♭"] "-"
         [:a {:href "#P-A"}  "A"]  "-"
         [:a {:href "#P-Bf"} "B♭"] "-"
         [:a {:href "#P-B"}  "B"]
     ]
     [:li "Triades majeures : "
         [:a {:href "#T-C"}  "C"]  "-"
         [:a {:href "#T-Df"} "D♭"] "-"
         [:a {:href "#T-D"}  "D"]  "-"
         [:a {:href "#T-Ef"} "E♭"] "-"
         [:a {:href "#T-E"}  "E"]  "-"
         [:a {:href "#T-F"}  "F"]  "-"
         [:a {:href "#T-Fs"} "F♯"] "-"
         [:a {:href "#T-Gf"} "G♭"] "-"
         [:a {:href "#T-G"}  "G"]  "-"
         [:a {:href "#T-Af"} "A♭"] "-"
         [:a {:href "#T-A"}  "A"]  "-"
         [:a {:href "#T-Bf"} "B♭"] "-"
         [:a {:href "#T-B"}  "B"]
     ]
     [:li "Gammes mineures : "
         [:a {:href "#m-C"}  "C"]  "-"
         [:a {:href "#m-Cs"} "C♯"] "-"
         [:a {:href "#m-D"}  "D"]  "-"
         [:a {:href "#m-Ds"} "D♯"]  "-"
         [:a {:href "#m-Ef"} "E♭"] "-"
         [:a {:href "#m-E"}  "E"]  "-"
         [:a {:href "#m-F"}  "F"]  "-"
         [:a {:href "#m-Fs"} "F♯"] "-"
         [:a {:href "#m-G"}  "G"]  "-"
         [:a {:href "#m-Gs"} "G♯"] "-"
         [:a {:href "#m-A"}  "A"]  "-"
         [:a {:href "#m-Bf"} "B♭"] "-"
         [:a {:href "#m-B"}  "B"]
     ]
     [:li "Pentas mineures : "
         [:a {:href "#p-C"}  "C"]  "-"
         [:a {:href "#p-Cs"} "C♯"] "-"
         [:a {:href "#p-D"}  "D"]  "-"
         [:a {:href "#p-Ds"} "D♯"]  "-"
         [:a {:href "#p-Ef"} "E♭"] "-"
         [:a {:href "#p-E"}  "E"]  "-"
         [:a {:href "#p-F"}  "F"]  "-"
         [:a {:href "#p-Fs"} "F♯"] "-"
         [:a {:href "#p-G"}  "G"]  "-"
         [:a {:href "#p-Gs"} "G♯"] "-"
         [:a {:href "#p-A"}  "A"]  "-"
         [:a {:href "#p-Bf"} "B♭"] "-"
         [:a {:href "#p-B"}  "B"]
     ]
     [:li "Triades mineures : "
         [:a {:href "#t-C"}  "C"]  "-"
         [:a {:href "#t-Cs"} "C♯"] "-"
         [:a {:href "#t-D"}  "D"]  "-"
         [:a {:href "#t-Ds"} "D♯"]  "-"
         [:a {:href "#t-Ef"} "E♭"] "-"
         [:a {:href "#t-E"}  "E"]  "-"
         [:a {:href "#t-F"}  "F"]  "-"
         [:a {:href "#t-Fs"} "F♯"] "-"
         [:a {:href "#t-G"}  "G"]  "-"
         [:a {:href "#t-Gs"} "G♯"] "-"
         [:a {:href "#t-A"}  "A"]  "-"
         [:a {:href "#t-Bf"} "B♭"] "-"
         [:a {:href "#t-B"}  "B"]
     ]
     [:li [:a {:href "#tricks"} "Ruses"]]
       [:ul
         [:li [:a {:href "#trick-do-re-mi"} "Do, Ré, Mi"]]
         [:li [:a {:href "#trick-si-do-re-mi-fa"} "Si, Do, Ré, Mi, Fa"]]
         [:li [:a {:href "#trick-fa-la-do"} "Fa, La, Do"]]
         [:li [:a {:href "#trick-5ta-min"}  "Penta mineure"]]]
     [:li [:a {:href "#topologie"} "Topologie du trombone"]
          " (visualisation des positions alternatives)"]
     [:li [:a {:href "#contact"} "Contact"]]
   ]

   [:h2 {:id "comment"} "Fonctionnement des schémas"]
   [:p "La coulisse est représentée par un trait vertical, position 1 en bas, "
       "position 7 en haut. Des petits marqueurs indiquent les positions 3 et "
       "5."]
   (add-pos-numbers (empty-slide2))
   [:p "Comme sur le trombone, les positions vont en s’écartant."]
   [:p "Un point de couleur indique à la fois :"]
   [:ul
     [:li "la position à donner à la coulisse ;"]
     [:li "l’harmonique à laquelle se situe la note (code couleur ci-dessous)."]
   ]
   [:p (rect 1) " 1"   [:sup "re"] " harmonique (tonique)"]
   [:p (rect 2) " 2"   [:sup "e"]  " harmonique (quinte)"]
   [:p (rect 3) " 3"   [:sup "e"]  " harmonique (octave)"]
   [:p (rect 4) " 4"   [:sup "e"]  " harmonique (tierce majeure de l’octave)"]
   [:p (rect 5) " 5"   [:sup "e"]  " harmonique (quinte de l’octave)"]
   [:p (rect 6) " 6"   [:sup "e"]  " harmonique (septième mineure de l’octave)"]
   [:p (rect 7) " 7"   [:sup "e"]  " harmonique (octave de l’octave)"]
   [:p (rect 8) " 8"   [:sup "e"]  " harmonique (seconde 2 octaves au dessus)"]
   [:p (rect 9) " 9"   [:sup "e"]  " harmonique (tierce majeure hyper haute)"]
   [:p (rect 10) " 10" [:sup "e"]  " harmonique (quarte augmentée hyper haute)"]
   [:p "Toutes les positions alternatives sont proposées, la position la "
       "plus petite est mise en évidence."]

   [:h2 {:id "chro"} "Gamme chromatique"]
   [:p "Il y a 44 notes pour 31 positions."]
                         E1 F1 Fs1 Gf1 G1 Gs1 Af1 A1 As1 Bf1 B1
   C2 Cs2 Df2 D2 Ds2 Ef2 E2 F2 Fs2 Gf2 G2 Gs2 Af2 A2 As2 Bf2 B2
   C3 Cs3 Df3 D3 Ds3 Ef3 E3 F3 Fs3 Gf3 G3 Gs3 Af3 A3 As3 Bf3

   [:h2 {:id "chro-asc"} "Gamme chromatique ascendante"]
   [:p "Voici 31 notes."]
                 E1 F1 Fs1 G1 Gs1 A1 As1 B1
   C2 Cs2 D2 Ds2 E2 F2 Fs2 G2 Gs2 A2 As2 B2
   C3 Cs3 D3 Ds3 E3 F3 Fs3 G3 Gs3 A3 As3

   [:h2 {:id "chro-desc"} "Gamme chromatique descendante"]
   [:p "Voici 31 notes."]
   Bf3 A3 Af3 G3 Gf3 F3 E3 Ef3 D3 Df3 C3
   B2 Bf2 A2 Af2 G2 Gf2 F2 E2 Ef2 D2 Df2 C2
   B1 Bf1 A1 Af1 G1 Gf1 F1 E1
   [:h2 {:id "une-pos"} "Notes n’ayant qu’une position"]
   [:p "Il y en a 20, réparties sur 14 positions."]
                         E1 F1 Fs1 Gf1 G1 Gs1 Af1 A1 As1 Bf1 B1
   C2 Cs2 Df2 D2 Ds2 Ef2       Fs2 Gf2 G2

   [:h2 {:id "deux-pos"} "Notes ayant deux positions"]
   [:p "Il y en a 13, réparties sur 9 positions."]
                         E2 F2
   Gs2 Af2 A2 As2 Bf2 B2
   C3 Cs3 Df3 Ds3 Ef3

   [:h2 {:id "trois-pos"} "Notes ayant trois positions"]
   [:p "Il y en a 7, réparties sur 6 positions."]
   D3 E3 F3 Fs3 Gf3 G3 A3

   [:h2 {:id "quatre-pos"} "Notes ayant quatre positions"]
   [:p "Il y en a 4, réparties sur 2 positions."]
   Gs3 Af3 As3 Bf3

   [:h2 {:id "maj"} "Gammes majeures"]
   [:h2 {:id "M-C"}  "C"]  E1 F1 G1 A1 B1 (sep)
                           C2 D2 E2 F2 G2 A2 B2 (sep)
                           C3 D3 E3 F3 G3 A3
   [:h2 {:id "M-Df"} "D♭"] F1 Gf1 Af1 Bf1 C2 (sep)
                           Df2 Ef2 F2 Gf2 Af2 Bf2 C3 (sep)
                           Df3 Ef3 F3 Gf3 Af3 Bf3
   [:h2 {:id "M-D"}  "D"]  E1 Fs1 G1 A1 B1 Cs2 (sep)
                           D2 E2 Fs2 G2 A2 B2 Cs3 (sep)
                           D3 E3 Fs3 G3 A3
   [:h2 {:id "M-Ef"} "E♭"] F1 G1 Af1 Bf1 C2 D2 (sep)
                           Ef2 F2 G2 Af2 Bf2 C3 D3 (sep)
                           Ef3 F3 G3 Af3 Bf3
   [:h2 {:id "M-E"}  "E"]  E1 Fs1 Gs1 A1 B1 Cs2 Ds2 (sep)
                           E2 Fs2 Gs2 A2 B2 Cs3 Ds3 (sep)
                           E3 Fs3 Gs3 A3
   [:h2 {:id "M-F"}  "F"]  E1 (sep)
                           F1 G1 A1 Bf1 C2 D2 E2 (sep)
                           F2 G2 A2 Bf2 C3 D3 E3 (sep)
                           F3 G3 A3 Bf3
   [:h2 {:id "M-Fs"} "F♯"] Es1 (sep)
                           Fs1 Gs1 As1 B1 Cs2 Ds2 Es2 (sep)
                           Fs2 Gs2 As2 B2 Cs3 Ds3 Es3 (sep)
                           Fs3 Gs3 As3
   [:h2 {:id "M-Gf"} "G♭"] F1 (sep)
                           Gf1 Af1 Bf1 Cf2 Df2 Ef2 F2 (sep)
                           Gf2 Af2 Bf2 Cf3 Df3 Ef3 F3 (sep)
                           Gf3 Af3 Bf3
   [:h2 {:id "M-G"}  "G"]  E1 Fs1 (sep)
                           G1 A1 B1 C2 D2 E2 Fs2 (sep)
                           G2 A2 B2 C3 D3 E3 Fs3 (sep)
                           G3 A3
   [:h2 {:id "M-Af"} "A♭"] F1 G1 (sep)
                           Af1 Bf1 C2 Df2 Ef2 F2 G2 (sep)
                           Af2 Bf2 C3 Df3 Ef3 F3 G3 (sep)
                           Af3 Bf3
   [:h2 {:id "M-A"}  "A"]  E1 Fs1 Gs1 (sep)
                           A1 B1 Cs2 D2 E2 Fs2 Gs2 (sep)
                           A2 B2 Cs3 D3 E3 Fs3 Gs3 (sep)
                           A3
   [:h2 {:id "M-Bf"} "B♭"] F1 G1 A1 (sep)
                           Bf1 C2 D2 Ef2 F2 G2 A2 (sep)
                           Bf2 C3 D3 Ef3 F3 G3 A3 (sep)
                           Bf3
   [:h2 {:id "M-B"}  "B"]  Fs1 Gs1 As1 (sep)
                           B1 Cs2 Ds2 E2 Fs2 Gs2 As2 (sep)
                           B2 Cs3 Ds3 E3 Fs3 Gs3 As3

   [:h2 {:id "5ta-maj"} "Gammes pentatoniques majeures"]
   [:h2 {:id "P-C"}  "C"]  E1 G1 A1 (sep)
                           C2 D2 E2 G2 A2 (sep)
                           C3 D3 E3 G3 A3
   [:h2 {:id "P-Df"} "D♭"] F1 Af1 Bf1 (sep)
                           Df2 Ef2 F2 Af2 Bf2 (sep)
                           Df3 Ef3 F3 Af3 Bf3
   [:h2 {:id "P-D"}  "D"]  E1 Fs1 A1 B1 (sep)
                           D2 E2 Fs2 A2 B2 (sep)
                           D3 E3 Fs3 A3
   [:h2 {:id "P-Ef"} "E♭"] F1 G1 Bf1 C2 (sep)
                           Ef2 F2 G2 Bf2 C3 (sep)
                           Ef3 F3 G3 Bf3
   [:h2 {:id "P-E"}  "E"]  E1 Fs1 Gs1 B1 Cs2 (sep)
                           E2 Fs2 Gs2 B2 Cs3 (sep)
                           E3 Fs3 Gs3
   [:h2 {:id "P-F"}  "F"]  E1 (sep)
                           F1 G1 A1 C2 D2 (sep)
                           F2 G2 A2 C3 D3 (sep)
                           F3 G3 A3
   [:h2 {:id "P-Fs"} "F♯"] Fs1 Gs1 As1 Cs2 Ds2 (sep)
                           Fs2 Gs2 As2 Cs3 Ds3 (sep)
                           Fs3 Gs3 As3
   [:h2 {:id "P-Gf"} "G♭"] Gf1 Af1 Bf1 Df2 Ef2 (sep)
                           Gf2 Af2 Bf2 Df3 Ef3 (sep)
                           Gf3 Af3 Bf3
   [:h2 {:id "P-G"}  "G"]  E1 (sep)
                           G1 A1 B1 D2 E2 (sep)
                           G2 A2 B2 D3 E3 (sep)
                           G3 A3
   [:h2 {:id "P-Af"} "A♭"] F1 (sep)
                           Af1 Bf1 C2 Ef2 F2 (sep)
                           Af2 Bf2 C3 Ef3 F3 (sep)
                           Af3 Bf3
   [:h2 {:id "P-A"}  "A"]  E1 Fs1 (sep)
                           A1 B1 Cs2 E2 Fs2 (sep)
                           A2 B2 Cs3 E3 Fs3 (sep)
                           A3
   [:h2 {:id "P-Bf"} "B♭"] F1 G1 (sep)
                           Bf1 C2 D2 F2 G2 (sep)
                           Bf2 C3 D3 F3 G3 (sep)
                           Bf3
   [:h2 {:id "P-B"}  "B"]  Fs1 Gs1 (sep)
                           B1 Cs2 Ds2 Fs2 Gs2 (sep)
                           B2 Cs3 Ds3 Fs3 Gs3

   [:h2 {:id "3des-maj"} "Triades majeures"]
   [:h2 {:id "T-C"}  "C"]  E1 G1 (sep)
                           C2 E2 G2 (sep)
                           C3 E3 G3
   [:h2 {:id "T-Df"} "D♭"] F1 Af1 (sep)
                           Df2 F2 Af2 (sep)
                           Df3 F3 Af3
   [:h2 {:id "T-D"}  "D"]  Fs1 A1 (sep)
                           D2 Fs2 A2 (sep)
                           D3 Fs3 A3
   [:h2 {:id "T-Ef"} "E♭"] G1 Bf1 (sep)
                           Ef2 G2 Bf2 (sep)
                           Ef3 G3 Bf3
   [:h2 {:id "T-E"}  "E"]  E1  Gs1 B1 (sep)
                           E2  Gs2 B2 (sep)
                           E3  Gs3
   [:h2 {:id "T-F"}  "F"]  F1 A1 C2 (sep)
                           F2 A2 C3 (sep)
                           F3 A3
   [:h2 {:id "T-Fs"} "F♯"] Fs1 As1 Cs2 (sep)
                           Fs2 As2 Cs3 (sep)
                           Fs3 As3
   [:h2 {:id "T-Gf"} "G♭"] Gf1 Bf1 Df2 (sep)
                           Gf2 Bf2 Df3 (sep)
                           Gf3 Bf3
   [:h2 {:id "T-G"}  "G"]  G1 B1 D2 (sep)
                           G2 B2 D3 (sep)
                           G3
   [:h2 {:id "T-Af"} "A♭"] Af1 C2 Ef2 (sep)
                           Af2 C3 Ef3 (sep)
                           Af3
   [:h2 {:id "T-A"}  "A"]  E1 (sep)
                           A1 Cs2 E2 (sep)
                           A2 Cs3 E3 (sep)
                           A3
   [:h2 {:id "T-Bf"} "B♭"] F1 (sep)
                           Bf1 D2 F2 (sep)
                           Bf2 D3 F3 (sep)
                           Bf3
   [:h2 {:id "T-B"}  "B"]  Fs1 (sep)
                           B1 Ds2 Fs2 (sep)
                           B2 Ds3 Fs3

   [:h2 {:id "min"} "Gammes mineures"]
   [:h2 {:id "m-C"}  "C"]  F1 G1 Af1 Bf1 (sep) C2 D2
                           Ef2 F2 G2 Af2 Bf2 (sep) C3 D3
                           Ef3 F3 G3 Af3 Bf3
   [:h2 {:id "m-Cs"} "C♯"] E1 Fs1 Gs1 A1 B1 (sep) Cs2 Ds2
                           E2 Fs2 Gs2 A2 B2 (sep) Cs3 Ds3
                           E3 Fs3 Gs3 A3
   [:h2 {:id "m-D"}  "D"]  E1
                           F1 G1 A1 Bf1 C2 (sep) D2 E2
                           F2 G2 A2 Bf2 C3 (sep) D3 E3
                           F3 G3 A3 Bf3
   [:h2 {:id "m-ds"} "D♯"] Es1
                           Fs1 Gs1 As1 B1 Cs2 (sep) Ds2 Es2
                           Fs2 Gs2 As2 B2 Cs3 (sep) Ds3 Es3
                           Fs3 Gs3 As3
   [:h2 {:id "m-Ef"} "E♭"] F1
                           Gf1 Af1 Bf1 Cf2 Df2 (sep) Ef2 F2
                           Gf2 Af2 Bf2 Cf3 Df3 (sep) Ef3 F3
                           Gf3 Af3 Bf3
   [:h2 {:id "m-E"}  "E"]  E1 Fs1
                           G1 A1 B1 C2 D2 (sep) E2 Fs2
                           G2 A2 B2 C3 D3 (sep) E3 Fs3
                           G3 A3
   [:h2 {:id "m-F"}  "F"]  F1 G1
                           Af1 Bf1 C2 Df2 Ef2 (sep) F2 G2
                           Af2 Bf2 C3 Df3 Ef3 (sep) F3 G3
                           Af3 Bf3
   [:h2 {:id "m-Fs"} "F♯"] E1 (sep) Fs1 Gs1
                           A1 B1 Cs2 D2 E2 (sep) Fs2 Gs2
                           A2 B2 Cs3 D3 E3 (sep) Fs3 Gs3
                           A3
   [:h2 {:id "m-G"}  "G"]  F1 (sep) G1 A1
                           Bf1 C2 D2 Ef2 F2 (sep) G2 A2
                           Bf2 C3 D3 Ef3 F3 (sep) G3 A3
                           Bf3
   [:h2 {:id "m-Gs"} "G♯"] Fs1 (sep) Gs1 As1
                           B1 Cs2 Ds2 E2 Fs2 (sep) Gs2 As2
                           B2 Cs3 Ds3 E3 Fs3 (sep) Gs3 As3
   [:h2 {:id "m-A"}  "A"]  E1 F1 G1 (sep) A1 B1
                           C2 D2 E2 F2 G2 (sep) A2 B2
                           C3 D3 E3 F3 G3 (sep) A3
   [:h2 {:id "m-Bf"} "B♭"] F1 Gf1 Af1 (sep) Bf1 C2
                           Df2 Ef2 F2 Gf2 Af2 (sep) Bf2 C3
                           Df3 Ef3 F3 Gf3 Af3 (sep) Bf3
   [:h2 {:id "m-B"}  "B"]  E1 Fs1 G1 A1 (sep) B1 Cs2
                           D2 E2 Fs2 G2 A2 (sep) B2 Cs3
                           D3 E3 Fs3 G3 A3

   [:h2 {:id "5ta-min"} "Gammes pentatoniques mineures"]
   [:h2 {:id "p-C"}  "C"]  F1 G1 Bf1 (sep) C2
                           Ef2 F2 G2 Bf2 (sep) C3
                           Ef3 F3 G3 Bf3
   [:h2 {:id "p-Cs"} "C♯"] E1 Fs1 Gs1 B1 (sep) Cs2
                           E2 Fs2 Gs2 B2 (sep) Cs3
                           E3 Fs3 Gs3
   [:h2 {:id "p-D"}  "D"]  F1 G1 A1 C2 (sep) D2
                           F2 G2 A2 C3 (sep) D3
                           F3 G3 A3
   [:h2 {:id "p-Ds"} "D♯"] Fs1 Gs1 As1 Cs2 (sep) Ds2
                           Fs2 Gs2 As2 Cs3 (sep) Ds3
                           Fs3 Gs3 As3
   [:h2 {:id "p-Ef"} "E♭"] Gf1 Af1 Bf1 Df2 (sep) Ef2
                           Gf2 Af2 Bf2 Df3 (sep) Ef3
                           Gf3 Af3 Bf3
   [:h2 {:id "p-E"}  "E"]  E1
                           G1 A1 B1 D2 (sep) E2
                           G2 A2 B2 D3 (sep) E3
                           G3 A3
   [:h2 {:id "p-F"}  "F"]  F1
                           Af1 Bf1 C2 Ef2 (sep) F2
                           Af2 Bf2 C3 Ef3 (sep) F3
                           Af3 Bf3
   [:h2 {:id "p-Fs"} "F♯"] E1 (sep) Fs1
                           A1 B1 Cs2 E2 (sep) Fs2
                           A2 B2 Cs3 E3 (sep) Fs3
                           A3
   [:h2 {:id "p-G"}  "G"]  F1 (sep) G1
                           Bf1 C2 D2 F2 (sep) G2
                           Bf2 C3 D3 F3 (sep) G3
                           Bf3
   [:h2 {:id "p-Gs"} "G♯"] Fs1 (sep) Gs1
                           B1 Cs2 Ds2 Fs2 (sep) Gs2
                           B2 Cs3 Ds3 Fs3 (sep) Gs3
   [:h2 {:id "p-A"}  "A"]  E1 G1 (sep) A1
                           C2 D2 E2 G2 (sep) A2
                           C3 D3 E3 G3 (sep) A3
   [:h2 {:id "p-Bf"} "B♭"] F1 Af1 (sep) Bf1
                           Df2 Ef2 F2 Af2 (sep) Bf2
                           Df3 Ef3 F3 Af3 (sep) Bf3
   [:h2 {:id "p-B"}  "B"]  E1 Fs1 A1 (sep) B1
                           D2 E2 Fs2 A2 (sep) B2
                           D3 E3 Fs3 A3

   [:h2 {:id "3des-min"} "Triades mineures"]
   [:h2 {:id "t-C"}  "C"]  G1 (sep) C2
                           Ef2 G2 (sep) C3
                           Ef3 G3
   [:h2 {:id "t-Cs"} "C♯"] E1 Gs1 (sep) Cs2
                           E2 Gs2 (sep) Cs3
                           E3 Gs3
   [:h2 {:id "t-D"}  "D"]  F1 A1 (sep) D2
                           F2 A2 (sep) D3
                           F3 A3
   [:h2 {:id "t-ds"} "D♯"] Fs1 As1 (sep) Ds2
                           Fs2 As2 (sep) Ds3
                           Fs3 As3
   [:h2 {:id "t-Ef"} "E♭"] Gf1 Bf1 (sep) Ef2
                           Gf2 Bf2 (sep) Ef3
                           Gf3 Bf3
   [:h2 {:id "t-E"}  "E"]  E1
                           G1 B1 (sep) E2
                           G2 B2 (sep) E3
                           G3
   [:h2 {:id "t-F"}  "F"]  F1
                           Af1 C2 (sep) F2
                           Af2 C3 (sep) F3
                           Af3
   [:h2 {:id "t-Fs"} "F♯"] Fs1
                           A1 Cs2 (sep) Fs2
                           A2 Cs3 (sep) Fs3
                           A3
   [:h2 {:id "t-G"}  "G"]  G1
                           Bf1 D2 (sep) G2
                           Bf2 D3 (sep) G3
                           Bf3
   [:h2 {:id "t-Gs"} "G♯"] Gs1
                           B1 Ds2 (sep) Gs2
                           B2 Ds3 (sep) Gs3
   [:h2 {:id "t-A"}  "A"]  E1 (sep) A1
                           C2 E2 (sep) A2
                           C3 E3 (sep) A3
   [:h2 {:id "t-Bf"} "B♭"] F1 (sep) Bf1
                           Df2 F2 (sep) Bf2
                           Df3 F3 (sep) Bf3
   [:h2 {:id "t-B"}  "B"]  Fs1 (sep) B1
                           D2 Fs2 (sep) B2
                           D3 Fs3

   [:h2 {:id "tricks"} "Ruses"]
   [:h3 {:id "trick-do-re-mi"} "Do, Ré, Mi"]
   (trick [Bf2 5 C3  6 D3  7]) (sep)
   (trick [B2  4 Cs3 5 Ds3 6]) (sep)
   (trick [C3  3 D3  4 E3  5]) (sep)
   (trick [Df3 2 Ef3 3 F3  4]) (sep)
   (trick [D3  1 E3  2 Fs3 3])
   [:h3 {:id "trick-si-do-re-mi-fa"} "Si, Do, Ré, Mi, Fa"]
   (trick [Bf2 1 C3  3 D3  1 Ef3 3 F3  1]) (sep)
   (trick [A2  2 B2  4 Cs3 2 D3  4 E3  2]) (sep)
   (trick [Af2 3 Bf2 5 C3  3 Df3 5 Ef3 3]) (sep)
   (trick [G2  4 A2  6 B2  4 C3  6 D3  4]) (sep)
   (trick [Gf2 5 Af2 7 Bf2 5 Cf3 7 Df3 5])
   [:h3 {:id "trick-fa-la-do"} "Fa, La, Do, Fa"]
   (trick [F2  1 A2  2 C3  3 F3  4]) (sep)
   (trick [E2  2 Gs2 3 B2  4 E3  5]) (sep)
   (trick [Ef2 3 G2  4 Bf2 5 Ef3 6]) (sep)
   (trick [D2  4 Fs2 5 A2  6 D3  7]) (sep)
   (trick [Df2 5 F2  6 Af2 7])
   [:h3 {:id "trick-5ta-min"} "Penta mineure"]
   [:p "♭7 1 ♭3 4 5 ♭7 1 ♭3 4"]
   (trick [Ef2 3 F2  1 Af2 3 Bf2 1 C3  3 Ef3 3 F3  1 Af3 3 Bf3 1]) [:br]
   (trick [D2  4 E2  2 G2  4 A2  2 B2  4 D3  4 E3  2 G3  4 A3  2]) [:br]
   (trick [Df2 5 Ef2 3 Gf2 5 Af2 3 Bf2 5 Df3 5 Ef3 3 Gf3 5 Af3 3]) [:br]
   (trick [C2  6 D2  4 F2  6 G2  4 A2  6 C3  6 D3  4 F3  6 G3  4]) [:br]
   (trick [B1  7 Cs2 5 E2  7 Fs2 5 Gs2 7 B2  7 Cs3 5 E3  7 Fs3 5])

   [:h2 {:id "topologie"} "Topologie"]
   [:p "Voici un schéma qui tente une visualisation des positions "
       "alternatives au trombone."]
   [:svg {:width W-topo :height H-topo}
     ; p7
     (trajet-l 1 7 2 7 (p-color 7))
     (trajet-c 2 7 2 2 0 -40 -20 -20 (p-color 7))
     (trajet-l 2 2 3 3 (p-color 7))
     (trajet-l 3 3 4 4 (p-color 7))
     (trajet-c 4 4 4 1 0 -30 -15 -20 (p-color 7))
     (trajet-l 4 1 5 2 (p-color 7))
     (trajet-l 5 2 6 3 (p-color 7) -2)
     (trajet-c 6 3 6 1 0 -30 -25 -23 (p-color 7))
     (trajet-l 6 1 7 1 (p-color 7) -2)
     ; p6
     (trajet-l 1 6 2 6 (p-color 6))
     (trajet-c 2 6 2 1 0 -40 -20 -20 (p-color 6))
     (trajet-l 2 1 3 2 (p-color 6))
     (trajet-l 3 2 4 3 (p-color 6))
     (trajet-l 4 3 5 3 (p-color 6) -1)
     (trajet-c 5 3 5 1 0 -30 -15 -20 (p-color 6))
     (trajet-l 5 1 6 2 (p-color 6) -2)
     (trajet-l 6 2 7 2 (p-color 6) -3)
     ; p5
     (trajet-l 1 5 2 5 (p-color 5))
     (trajet-l 2 5 3 5 (p-color 5))
     (trajet-c 3 5 3 1 0 -30 -15 -20 (p-color 5))
     (trajet-l 3 1 4 2 (p-color 5))
     (trajet-l 4 2 5 2 (p-color 5) -1)
     (trajet-l 5 2 6 3 (p-color 5) 2)
     (trajet-c 6 3 6 1 0 -25 -17 -20 (p-color 5))
     (trajet-l 6 1 7 1 (p-color 5) 1)
     ; p4
     (trajet-l 1 4 2 4 (p-color 4))
     (trajet-l 2 4 3 4 (p-color 4))
     (trajet-l 3 4 4 4 (p-color 4))
     (trajet-c 4 4 4 1 0 -20 -10 -15 (p-color 4))
     (trajet-l 4 1 5 1 (p-color 4) -1)
     (trajet-l 5 1 6 2 (p-color 4) 2)
     (trajet-l 6 2 7 2 (p-color 4))
     ; p3
     (trajet-l 1 3 2 3 (p-color 3))
     (trajet-l 2 3 3 3 (p-color 3))
     (trajet-l 3 3 4 3 (p-color 3))
     (trajet-l 4 3 5 3 (p-color 3) 2)
     (trajet-l 5 3 6 3 (p-color 3))
     (trajet-c 6 3 6 1 0 -25 -12 -15 (p-color 3))
     (trajet-l 6 1 7 1 (p-color 3) -5)
     ; p2
     (trajet-l 1 2 2 2 (p-color 2))
     (trajet-l 2 2 3 2 (p-color 2))
     (trajet-l 3 2 4 2 (p-color 2))
     (trajet-l 4 2 5 2 (p-color 2) 1)
     (trajet-l 5 2 6 2 (p-color 2))
     (trajet-l 6 2 7 2 (p-color 2) 3)
     ; p1
     (trajet-l 1 1 2 1 (p-color 1))
     (trajet-l 2 1 3 1 (p-color 1))
     (trajet-l 3 1 4 1 (p-color 1))
     (trajet-l 4 1 5 1 (p-color 1) 2)
     (trajet-l 5 1 6 1 (p-color 1))
     (trajet-l 6 1 7 1 (p-color 1) 4)
     (map (comp point hp-to-point)
          (concat (map vector (repeat 1) (range1 7))
                  (map vector (repeat 2) (range1 7))
                  (map vector (repeat 3) (range1 5))
                  (map vector (repeat 4) (range1 4))
                  (map vector (repeat 5) (range1 3))
                  (map vector (repeat 6) (range1 3))
                  (map vector (repeat 7) (range1 2))))]

   [:h2 {:id "contact"} "Contact"]
   [:p "Pour toute remarque ou suggestion, vous pouvez :"]
   [:ul
     [:li "créer un ticket sur " [:a {:href "https://github.com/Grahack/bonepos"}
                                     "Github"] " ;"]
     [:li "envoyer un mail à " [:a {:href "mailto:profgra.org@gmail.com"}
                                   "profgra.org@gmail.com"] "."]
   ]
   ])

(defn mount [el]
  (reagent/render-component [hello-world] el))

(defn mount-app-element []
  (when-let [el (get-app-element)]
    (mount el)))

(mount-app-element)

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-app-element)
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
