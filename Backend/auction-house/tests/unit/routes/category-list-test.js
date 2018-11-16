import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | category-list', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:category-list');
    assert.ok(route);
  });
});
