package org.akka.essentials.wc.mapreduce.example.server

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorRef

sealed trait MapReduceMessage

case class WordCount(val word: String, val count: Integer) extends MapReduceMessage
case class Result extends MapReduceMessage
case class MapData(val dataList: List[WordCount]) extends MapReduceMessage
case class ReduceData(val reduceDataMap: Map[String, Int]) extends MapReduceMessage

class MapActor(actor: ActorRef) extends Actor with ActorLogging {

  val STOP_WORDS = List("a", "about", "above", "above", "across", "after",
    "afterwards", "again", "against", "all", "almost", "alone",
    "along", "already", "also", "although", "always", "am", "among",
    "amongst", "amoungst", "amount", "an", "and", "another", "any",
    "anyhow", "anyone", "anything", "anyway", "anywhere", "are",
    "around", "as", "at", "back", "be", "became", "because", "become",
    "becomes", "becoming", "been", "before", "beforehand", "behind",
    "being", "below", "beside", "besides", "between", "beyond", "bill",
    "both", "bottom", "but", "by", "call", "can", "cannot", "cant",
    "co", "con", "could", "couldnt", "cry", "de", "describe", "detail",
    "do", "done", "down", "due", "during", "each", "eg", "eight",
    "either", "eleven", "else", "elsewhere", "empty", "enough", "etc",
    "even", "ever", "every", "everyone", "everything", "everywhere",
    "except", "few", "fifteen", "fify", "fill", "find", "fire",
    "first", "five", "for", "former", "formerly", "forty", "found",
    "four", "from", "front", "full", "further", "get", "give", "go",
    "had", "has", "hasnt", "have", "he", "hence", "her", "here",
    "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
    "him", "himself", "his", "how", "however", "hundred", "ie", "if",
    "in", "inc", "indeed", "interest", "into", "is", "it", "its",
    "itself", "keep", "last", "latter", "latterly", "least", "less",
    "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill",
    "mine", "more", "moreover", "most", "mostly", "move", "much",
    "must", "my", "myself", "name", "namely", "neither", "never",
    "nevertheless", "next", "nine", "no", "nobody", "none", "noone",
    "nor", "not", "nothing", "now", "nowhere", "of", "off", "often",
    "on", "once", "one", "only", "onto", "or", "other", "others",
    "otherwise", "our", "ours", "ourselves", "out", "over", "own",
    "part", "per", "perhaps", "please", "put", "rather", "re", "same",
    "see", "seem", "seemed", "seeming", "seems", "serious", "several",
    "she", "should", "show", "side", "since", "sincere", "six",
    "sixty", "so", "some", "somehow", "someone", "something",
    "sometime", "sometimes", "somewhere", "still", "such", "system",
    "take", "ten", "than", "that", "the", "their", "them",
    "themselves", "then", "thence", "there", "thereafter", "thereby",
    "therefore", "therein", "thereupon", "these", "they", "thickv",
    "thin", "third", "this", "those", "though", "three", "through",
    "throughout", "thru", "thus", "to", "together", "too", "top",
    "toward", "towards", "twelve", "twenty", "two", "un", "under",
    "until", "up", "upon", "us", "very", "via", "was", "we", "well",
    "were", "what", "whatever", "when", "whence", "whenever", "where",
    "whereafter", "whereas", "whereby", "wherein", "whereupon",
    "wherever", "whether", "which", "while", "whither", "who",
    "whoever", "whole", "whom", "whose", "why", "will", "with",
    "within", "without", "would", "yet", "you", "your", "yours",
    "yourself", "yourselves", "the")

  // message handler
  def receive = {
    case work: String =>
      // perform the work
      val list = evaluateExpression(work);

      // reply with the result
      actor ! list;
    case x => throw new IllegalArgumentException("Unknown message "+x);
  }

    def evaluateExpression(line: String): MapData = MapData {
    line.split("""\s+""").toList.foldLeft(List[WordCount]()) {
      (index, word) =>
        if (!STOP_WORDS.contains(word.toLowerCase))
          WordCount(word.toLowerCase, 1):: index
        else
          index
    }
  }

}