import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | account/sales', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:account/sales');
    assert.ok(route);
  });
});
