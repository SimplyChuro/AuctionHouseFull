import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | account/sales/sold', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:account/sales/sold');
    assert.ok(route);
  });
});
