curl -XGET -S 'http://localhost:9200/_analyze?analyzer=whitespace&pretty=true' -d 'Red-orange gerbils live at #43A Franklin St.'

'query':{
	'match_all':{} # select * from events;
}


'query':{
	'term':{
		'city': 'Nashville'
	} 
}

'query':{
	'match':{
		'name': 'sort filbert'
	} 
}

'query':{
	'match_phrase':{
		'name': 'sorting filbert'
	} 
}

'query':{
	'match_phrase':{
		'name': 'filbert fun', 'slop': 2 # loose phrase
	} 
}

# complex queries with bool

'query':{
	'bool':{
		'must':[
			{
				'term':{
					'city': 'Nashville'
				}
			}
		],
		'should':[
			{
				'match_phrase':{
					'name':{
						'query': 'filbert fun',
						'slop':2,
						'boost': 2
					}
				}
			}
		]
	}
}

************************

'query':{
	'match_all':{}

},
'aggs':{
	'by_city':{
		'terms':{
			'field':'city'
		}
	},
	'by_price':{
		'histogram':{
			'field': 'price',
			'interval':10
		}
	}
}

************

'query':{
	'match_all':{}

},
'aggs':{
	'by_city':{
		'terms':{
			'field':'city'
		}
	},
	'aggs':{
		'by_price':{
			'histogram':{
				'field': 'price',
				'interval':10
			}
		}
	}
}

