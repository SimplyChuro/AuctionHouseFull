import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | shop/privacy-and-policy', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:shop/privacy-and-policy');
    assert.ok(route);
  });
});
