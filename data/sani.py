#!/usr/bin/env python
"""
This script take a file of data exported from the 
application after the F12 season and converts it 
to the schema used for the F13 season.
"""

import json, random
random.seed(1)

f = open('data.json', 'r')
data = json.load(f)
#print json.dumps(data, sort_keys=True, indent=4, separators=(',', ': '))
absences = data['absences']
appdata = data['appData']
versions = data['versions']
events = data['events']
forms = data['forms']
messages = data['messages']
mobiledata = data['mobileData']
users = data['users']

#print json.dumps(absences, sort_keys=True, indent=2, separators=(',', ': '))

idfile = open('identities.txt', 'r')
newids = {}
newidentities = {}
for line in idfile:
  first, last, netid, iastate, gmail, major, uid = line.split("\t")
  uid = uid.strip()
  #print first + " " + last + " " + netid + " " + iastate + " " + gmail + " " + major
  newidentities[netid] = {'first':first, 'last':last, 'netid':netid, 'iastate':iastate, 'gmail':gmail, 'major':major, 'uid':uid}
#print newidentities
usednewnetids = []

"""give each old netid a new identity"""
oldtonew = {}
for obj in users:
  newid = random.choice(newidentities.keys())
  while newid in usednewnetids:
    newid = random.choice(newidentities.keys())

  #note that these will not be the correct grades.
  #grades will only be correct after recalculating for each user
  obj['grade'] = obj['grade'][0] #strip plus/minus
  usednewnetids.append(newid)
  oldtonew[obj['id']] = newid

"""start reassigning"""
for obj in users:
  newid = oldtonew[obj['id']]
  u = newidentities[newid]
  obj['id'] = u['netid']
  obj['email']['email'] = u['iastate']
  obj['secondEmail']['email'] = u['gmail']
  obj['universityID'] = u['uid']
  obj['firstName'] = u['first']
  obj['lastName'] = u['last']
  obj['major'] = u['major']
#print json.dumps(users, sort_keys=True, indent=2, separators=(',', ': '))

for obj in absences:
  newid = oldtonew[obj['student']['id']]
  u = newidentities[newid]
  obj['student']['id'] = u['netid']
  del obj['messages']
  #TODO kill this field, don't just nullify it
#print json.dumps(absences, sort_keys=True, indent=2, separators=(',', ': '))  

#remove form C
newforms = [x for x in forms if x['type'] != 'C']

for obj in newforms:
  newid = oldtonew[obj['student']['id']]
  u = newidentities[newid]
  del obj['messages']
  del obj['details']
  obj['emailStatus'] = ""
  obj['emailTo'] = ""
  del obj ['emailStatus']
  del obj['emailTo']

  obj['details'] = ""
  obj['student']['id'] = u['netid']
  if (obj['type'] == 'A'):
    obj['type'] = 'PerformanceAbsence'
  elif (obj['type'] == 'B'):
    obj['type'] = 'ClassConflict'
  elif (obj['type'] == 'D'):
    obj['type'] = 'TimeWorked'

del data['appData']
data['absences'] = absences
data['forms'] = newforms
data['events'] = events
del data['messages']
data['users'] = users
del data['mobileData']


print json.dumps(data, sort_keys=True, indent=2, separators=(',', ': '))
