import re
from gensim.parsing.porter import PorterStemmer
from gensim.models.doc2vec import Doc2Vec, TaggedDocument
from gensim.test.utils import get_tmpfile
from gensim.parsing.preprocessing import remove_stopwords

infile = open('myOut2.txt', 'r')
lines = infile.read().splitlines()
lines = map(lambda x : re.sub(r"[^A-Za-z\s]", '', x), lines)
lines = map(lambda x : remove_stopwords(x), lines)
#p = PorterStemmer()
#lines = map(lambda x : p.stem_sentence(x), lines)

print("done preprocess")

documents = [TaggedDocument(doc, [i]) for i, doc in enumerate(lines)]
model = Doc2Vec(vector_size=30, window=10, min_count=2, workers=3)

print("build vocab")
model.build_vocab(documents)

print("start train")
model.train(documents, total_examples=model.corpus_count, epochs=15)

model.delete_temporary_training_data(keep_doctags_vectors=True, keep_inference=True)
model.save("../models/imdb_model.bin")