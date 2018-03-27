import React, { Component } from 'react';

class AddFavorites extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            retailers: this.props.retailers,
            newFavorite: {
                orgName: this.props.selected.cnbio_org_name,
                primaryName: this.props.selected.primary,
                secondaryName: this.props.selected.secondary,
                lat: this.props.selected.latitude,
                lon: this.props.selected.longitude
            }
        }
    }

    //component created to save the data to add a new favorite in state, and then pass that data along to the method that adds a new favorite
    crudChange(newFav) {
        this.props.addFavorite(newFav);
    }

    render() { 
        return ( 
            <div>
                <button
                    onClick={() => this.crudChange(this.state.newFavorite)}>
                    Favorite
                </button>
                <hr />
                <hr />
            </div>
         )
    }
}
 
export default AddFavorites;