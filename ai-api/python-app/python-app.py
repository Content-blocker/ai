import sys
import os
import gensim
from gensim import models
from gensim.models import doc2vec
from collections import defaultdict
from gensim import corpora
from scipy import spatial
from gensim.similarities import MatrixSimilarity

stop_list = ['ourselves', 'hers', 'between', 'yourself', 'but', 'again', 'there', 'about', 'once', 'during', 'out',
             'very', 'having', 'with', 'they', 'own', 'an', 'be', 'some', 'for', 'do', 'its', 'yours', 'such', 'into', 'of',
             'most', 'itself', 'other', 'off', 'is', 's', 'am', 'or', 'who', 'as', 'from', 'him', 'each', 'the', 'themselves',
             'until', 'below', 'are', 'we', 'these', 'your', 'his', 'through', 'don', 'nor', 'me', 'were', 'her', 'more',
             'himself', 'this', 'down', 'should', 'our', 'their', 'while', 'above', 'both', 'up', 'to', 'ours', 'had', 'she', 'all', 'no',
             'when', 'at', 'any', 'before', 'them', 'same', 'and', 'been', 'have', 'in', 'will', 'on', 'does', 'yourselves', 'then',
             'that', 'because', 'what', 'over', 'why', 'so', 'can', 'did', 'not', 'now', 'under', 'he', 'you', 'herself', 'has',
             'just', 'where', 'too', 'only', 'myself', 'which', 'those', 'i', 'after', 'few', 'whom', 'being', 'if', 'theirs',
             'my', 'against', 'a', 'by', 'doing', 'it', 'how', 'was', 'here', 'than']

documentsdemo = ["At King's Landing, the wight is presented to the Lannisters",
             "Cersei demands Jon's neutrality in the Great War, but he upholds his oath to Daenerys, provoking Cersei to end discussions",
             "Tyrion meets privately with Cersei, apparently gaining her alliance",
             "Cersei later reveals to Jaime that she lied and really intends to use the Golden Company of Braavos to secure her hold on Westeros",
             "Disgusted, Jaime deserts her and rides north",
             "Jon Snow dies",
             "Aboard a ship bound for White Harbor, Jon and Daenerys make love",
             "At Dragonstone, Theon earns his men's respect and leads them to rescue Yara",
             "At Winterfell, Littlefinger sows dissent by exploiting Arya's threatening demeanor toward Sansa, leading to a trial",
             "To his surprise, a united Sansa, Arya, and Bran accuse Littlefinger of murder, conspiracy, and treason, which Bran confirms with his visions",
             "Deserted by the Lords of the Vale, Littlefinger is sentenced to death by Sansa and executed by Arya",
             "Samwell arrives at Winterfell and meets with Bran, where both discuss Jon's parentage",
             "Through Sam's earlier research and Bran's visions, they now realize Jon is a trueborn Targaryen named Aegon and the legitimate heir to the Iron Throne, as his parents  Rhaegar Targaryen and Lyanna Stark  married in secret",
             "At Eastwatch, the Night King, astride the undead Viserion, blasts the Wall with blue dragon fire, creating a hole for the Army of the Dead to march through"]


def loadModel(model):
    return doc2vec.Doc2Vec.load(model)


def analyse(plotin, queryin, d2v_model):
    documents = plotin
    querytext = queryin
    query = [word for word in querytext.replace("'s", "").replace(",", "").lower().split() if word not in stop_list]
    sum = 0
    max = 0
    for comptext in documents:
        ct = [word for word in comptext.replace("'s", "").replace(",", "").lower().split() if word not in stop_list]
        vec1 = d2v_model.infer_vector(query)
        vec2 = d2v_model.infer_vector(ct)
        similarity = 1 - spatial.distance.cosine(vec1, vec2)
        sum += similarity
        if max < similarity:
            max = similarity
        #print(similarity);
    return sum / len(documents), max


if __name__ == "__main__":
    model = loadModel(sys.argv[1])
    while True:
        s = sys.stdin.readline().strip()
        avg, max = analyse(documentsdemo, s, model)
        ret = str(avg) + "," + str(max)
        sys.stdout.write(ret + '\n')
        sys.stdout.flush()


###SECOND TRY
	#texts = [
	#	[word for word in document.replace("'s","").replace(",","").lower().split() if word not in stoplist]
	#	for document in documents
	#]

	#frequency = defaultdict(int)
	#for text in texts:
	#    for token in text:
	#        frequency[token] += 1
	#texts = [[token for token in text if frequency[token] > 1]
	#         for text in texts]

	#query = [word for word in querytext.replace("'s","").replace(",","").lower().split() if word not in stoplist]
	#dictionary = corpora.Dictionary(texts)
	#corpus = [dictionary.doc2bow(text) for text in texts]
	#lsi = models.LsiModel(corpus, id2word=dictionary, num_topics=50)
	#vec_bow = dictionary.doc2bow(querytext.lower().split())
	#vec_lsi = lsi[vec_bow]  # convert the query to LSI space
	#index = MatrixSimilarity(lsi[corpus])
	#sims = index[vec_lsi]
	#sims = sorted(enumerate(sims), key=lambda item: -item[1])
	#pprint(sims)