import Controller from '@ember/controller';

export default Controller.extend({

  productList: Ember.computed(function () {
    return this.store.peekAll(products);
  }),

});
