import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | account/sell/sale-item', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:account/sell/sale-item');
    assert.ok(route);
  });
});
