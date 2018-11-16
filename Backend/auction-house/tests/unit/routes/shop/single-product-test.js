import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | shop/single-product', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:shop/single-product');
    assert.ok(route);
  });
});
