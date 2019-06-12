import Controller from '@ember/controller';

export default Controller.extend({
  selectedList: 'new',

  actions: {
    setSelectedListType: function(type) {
      this.set('selectedList', type);
    }
  }
});
