import DS from 'ember-data';

export default DS.Model.extend({
  name: DS.attr('string'),
  parent_id: DS.attr('number'),
  isParent: Ember.computed('parent_id', function() {
    var checker = this.get('parent_id');
    if (checker === null) {
    	 return  true;
    }else{
		return false;
    }
  })
});
