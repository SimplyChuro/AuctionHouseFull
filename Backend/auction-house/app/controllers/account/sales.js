import Controller from '@ember/controller';

export default Controller.extend({
  saleStatus: 'active',
  actions: {
    setSaleStatus: function(selected) {
      this.set('saleStatus', selected)
    }
  }
});
