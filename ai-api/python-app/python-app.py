import sys
import gensim
from pprint import pprint
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


def loadModel(file):
    return doc2vec.Doc2Vec.load('models/enwiki_dbow/doc2vec.bin')


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
        print(similarity)
    return sum / len(documents), max


if __name__ == "__main__":
    print(str(sys.argv), file=sys.stderr)
    loadModel(sys.argv[0])
    while True:
        s = sys.stdin.readline().strip()
        sys.stdout.write(s.upper() + 'python OP' + '\n')
        sys.stdout.flush()
