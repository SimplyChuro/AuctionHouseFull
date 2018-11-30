import Controller from '@ember/controller';

export default Controller.extend({
  displayDetails: false,
  currentCategory: 0,
  selectedOption: null,
  selectedColor: null,
  selectedSize: null,
  selectedListType: null,

  filteredProducts: Ember.computed('selectedOption', function(){
    var selI = this.get('selectedOption');
    var products = this.get('model.productList');
    if(this.get('selectedOption') !== null) {
      return products.filter(
        function(product) { 
          let categories = product.get('productcategory');
          let everyProduct = null;
          categories.forEach(function(item){
            if((item.get('category').get('name') == selI.name)&&(item.get('category').get('id') == selI.id)){
              everyProduct = product;
            } else {
              return;
            }
          });
          return everyProduct;
        });
    }else{
      return products;
    }
  }),

  actions: {
    toggleDetails: function(category) {
      var rowID = category.id;
      if(rowID !== this.get('currentCategory')){
        this.set('currentCategory', rowID);
        this.set('displayDetails', true);
      }else{
        this.toggleProperty('displayDetails');
      }
      return;
    },

    setSelection: function(selected) {
      this.set('selectedOption', selected);  
    },

    setColor: function(selected) {
      this.set('selectedColor', selected);
    },

    setSize: function(selected) {
      this.set('selectedSize', selected);  
    },

    setListType: function(selected) {
      this.set('selectedListType', selected);  
    },

    createWishlist: function(productID) {
      this.store.createRecord('wishlist', {
        status: 'active',
        product_id: productID
      }).save();
    },

    clearFields: function(){
      this.set('currentCategory', 0);
      this.set('selectedOption', null);
      this.set('selectedColor', null); 
      this.set('selectedSize', null); 
      this.set('selectedListType', null); 
    }
  }
});
