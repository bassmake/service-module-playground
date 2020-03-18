assert json.name == '${customerName}'
assert json.points == 0
assert json.tier == 'NONE'

context.setVariable("customerId", json.id)