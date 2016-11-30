# TD-KdTree

## Fonction buildTree au 30/11/2016

Mon code KdTree fonctionne actuellement pour les deux dimensions possibles (2 et 3).  
J'ai également écrit du code pour la gestion de la max_depth, dont le fonctionnement est vérifié et testé.    
  
J'ai implémenté dans la classe Main un code de quantization d'image utilisant des KdTree.  
Celui-ci fonctionne et est capable de créer, à partir d'une image donnée, la même image mais en très peu de couleur.  
   
Voici quelques images illustrant cette transformation, ici en 16 couleurs :   
   
Image originel / image transformée en 16 couleur / palette des 16 couleurs utilisées   
   
![chat1](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat1/chat1.jpg "chat1")
![ResColor_chat1](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat1/ResColor.jpg "ResColor_chat1")
![PaletteColor_chat1](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat1/PaletteColor.jpg "PaletteColor_chat1")  
   
![chat2](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat2/chat2.jpg "chat2")
![ResColor_chat2](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat2/ResColor.jpg "ResColor_chat2")
![PaletteColor_chat2](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat2/PaletteColor.jpg "PaletteColor_chat2")  
   
![chat3](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat3/chat3.jpg "chat3")
![ResColor_chat3](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat3/ResColor.jpg "ResColor_chat3")
![PaletteColor_chat3](https://github.com/Quente59/TD-KdTree/blob/Work/tests/chat3/PaletteColor.jpg "PaletteColor_chat3")  
   
