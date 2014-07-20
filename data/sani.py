#!/usr/bin/env python
"""
This script takes a file of data exported after 
the F13 season and anonymizes it.

Updated on 7/19/14 for the 2013 schema.
"""

import json, random
random.seed(1)

f = open('data.json', 'r')
data = json.load(f)
#print json.dumps(data, sort_keys=True, indent=4, separators=(',', ': '))
absences = data['absences']
appdata = data['appData']
events = data['events']
forms = data['forms']
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
usednewnetids = set()

"""give each old netid a new identity"""
oldtonew = {}
for obj in users:
  newid = random.choice(newidentities.keys())
  while newid in usednewnetids:
    newid = random.choice(newidentities.keys())

  usednewnetids.add(newid)
  oldtonew[obj['id']] = newid

#start reassigning
for obj in users:
  newid = oldtonew[obj['id']]
  u = newidentities[newid]
  obj['id'] = u['netid']
  obj['email']['email'] = u['iastate']
  obj['secondEmail'] = {}
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
#print json.dumps(absences, sort_keys=True, indent=2, separators=(',', ': '))  

for obj in forms:
  newid = oldtonew[obj['student']['id']]
  u = newidentities[newid]
  del obj['details']
  obj['emailStatus'] = ""
  obj['emailTo'] = ""
  del obj ['emailStatus']
  del obj['emailTo']
  obj['details'] = ""
  obj['student']['id'] = u['netid']

del data['appData']
data['absences'] = absences
data['forms'] = forms
data['events'] = events
data['users'] = users
del data['mobileData']

print json.dumps(data, sort_keys=True, indent=2, separators=(',', ': '))
