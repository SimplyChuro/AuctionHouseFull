import { module, test } from 'qunit';
import { setupTest } from 'ember-qunit';

module('Unit | Route | account/sell/new', function(hooks) {
  setupTest(hooks);

  test('it exists', function(assert) {
    let route = this.owner.lookup('route:account/sell/new');
    assert.ok(route);
  });
});
