import React, { Component } from 'react';
import { Button, Glyphicon } from 'react-bootstrap';

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
                <Button className="buttonTwo" style={{fontSize: '15px'}} onClick={() => this.crudChange(this.state.newFavorite)}>
                    <Glyphicon glyph="star" />
                </Button>
            </div>
         )
    }
}
 
export default AddFavorites;