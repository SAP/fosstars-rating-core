import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import textwrap


# returns a test vector (dict) with all unknown values
def all_unknown(features, alias, score_from = 0.0, score_to = 1.0, label = None):
    values = {
        'alias': alias,
        'score_from': score_from,
        'score_to': score_to,
        'label': label
    }
    for feature in features:
        values[feature] = 'unknown'
    return values


# a base class for test vectors
class BaseTestVector:

    def __init__(self, features, alias = ''):
        self.values = all_unknown(features, alias)

    def copy(self, vector):
        if not isinstance(vector, BaseTestVector):
            raise Exception('Not a test vector! ({})'.format(type(vector)))
        for key in vector.values:
            if key == 'alias':
                continue
            self.values[key] = vector.values[key]
        return self

    def score_from(self, value):
        self.values['score_from'] = value
        return self

    def score_to(self, value):
        self.values['score_to'] = value
        return self

    def set(self, feature, value):
        self.values[feature] = value
        return self

    def make(self):
        return self.values


class TestVectorList:

    def __init__(self, features):
        self.data = []
        self.alias_counter = 0
        self.features = features

    # register a test vector
    def register(self, test_vector, pretty=False):
        if isinstance(test_vector, dict):
            values = test_vector
        elif isinstance(test_vector, BaseTestVector):
            values = test_vector.make()
        else:
            raise Exception('Unexpected object: {}'.format(type(test_vector)))
        if 'alias' not in values or values['alias'] == None or values['alias'] == '':
            values['alias'] = 'test_vector_{}'.format(self.alias_counter)
            self.alias_counter = self.alias_counter + 1
        self.data.append(values)
        print('registered: {}'.format(values['alias']))
        if pretty:
            pprint.PrettyPrinter(indent=4, depth=1).pprint(values)

    # checks if two vectors have the same values
    def check_same_values(self, first, second):
        found = False
        for key in first:
            if key in ['alias', 'score_from', 'score_to', 'label']:
                continue
            if key not in second:
                raise Exception('Missing feature: {}'.format(key))
            if first[key] != second[key]:
                found = True
        if not found:
            raise Exception('{} and {} are duplicates'.format(first['alias'], second['alias']))

    # checks duplicate vectors
    def check_duplicates(self, vector):
        for another in self.data:
            if vector['alias'] != another['alias']:
                self.check_same_values(vector, another)

    # check if all test vectors have all required fields
    def check(self):
        aliases = set()
        for vector in self.data:
            if 'alias' not in vector:
                raise Exception('Missing alias!')
            if vector['alias'] in aliases:
                raise Exception('Duplicate alias "{}"!'.format(vector['alias']))
            aliases.add(vector['alias'])
            if 'score_from' not in vector:
                raise Exception('Missing score_from!')
            if vector['score_from'] < 0 or vector['score_from'] > 10:
                raise Exception('Wrong score_from!')
            if vector['score_to'] < 0 or vector['score_to'] > 10:
                raise Exception('Wrong score_to!')
            if vector['score_from'] >= vector['score_to']:
                raise Exception('score_from is greater than score_to!')
            if 'score_to' not in vector:
                raise Exception('Missing score_to!')
            for feature in self.features:
                if feature not in vector:
                    raise Exception('Missing feature "{}"'.format(feature))
            self.check_duplicates(vector)

    def make_data_frame(self):
        return pd.DataFrame(self.data)


# draw histograms
def draw_hists(columns, test_vectors, width=20, height=5):
    n = len(columns)
    fig, axes = plt.subplots(1, n)
    fig.set_figwidth(width)
    fig.set_figheight(height)
    i = 0
    for column in columns:
        test_vectors[column].value_counts().plot(kind='bar', rot=0, ax=axes[i])
        axes[i].set_title(textwrap.TextWrapper(width=25).fill(column))
        i = i + 1
