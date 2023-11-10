package com.adifaisalr.core.domain.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Ability(
    val id: Int,
    val name: String,
    val isMainSeries: Boolean,
    val generation: NamedApiResource,
    val names: List<Name>,
    val effectEntries: List<VerboseEffect>,
    val effectChanges: List<AbilityEffectChange>,
    val flavorTextEntries: List<AbilityFlavorText>,
    val pokemon: List<AbilityPokemon>
)

data class AbilityEffectChange(
    val effectEntries: List<Effect>,
    val versionGroup: NamedApiResource
)

data class AbilityFlavorText(
    val flavorText: String,
    val language: NamedApiResource,
    val versionGroup: NamedApiResource
)

data class AbilityPokemon(
    val isHidden: Boolean,
    val slot: Int,
    val pokemon: NamedApiResource
)

data class Characteristic(
    val id: Int,
    val geneModulo: Int,
    val possibleValues: List<Int>,
    val descriptions: List<Description>
)

data class EggGroup(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val pokemonSpecies: List<NamedApiResource>
)

data class Gender(
    val id: Int,
    val name: String,
    val pokemonSpeciesDetails: List<PokemonSpeciesGender>,
    val requiredForEvolution: List<NamedApiResource>
)

data class PokemonSpeciesGender(
    val rate: Int,
    val pokemonSpecies: NamedApiResource
)

data class GrowthRate(
    val id: Int,
    val name: String,
    val formula: String,
    val descriptions: List<Description>,
    val levels: List<GrowthRateExperienceLevel>,
    val pokemonSpecies: List<NamedApiResource>
)

data class GrowthRateExperienceLevel(
    val level: Int,
    val experience: Int
)

data class Nature(
    val id: Int,
    val name: String,
    val decreasedStat: NamedApiResource?,
    val increasedStat: NamedApiResource?,
    val hatesFlavor: NamedApiResource?,
    val likesFlavor: NamedApiResource?,
    val pokeathlonStatChanges: List<NatureStatChange>,
    val moveBattleStylePreferences: List<MoveBattleStylePreference>,
    val names: List<Name>
)

data class NatureStatChange(
    val maxChange: Int,
    val pokeathlonStat: NamedApiResource
)

data class MoveBattleStylePreference(
    val lowHpPreference: Int,
    val highHpPreference: Int,
    val moveBattleStyle: NamedApiResource
)

data class PokeathlonStat(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val affectingNatures: NaturePokeathlonStatAffectSets
)

data class NaturePokeathlonStatAffectSets(
    val increase: List<NaturePokeathlonStatAffect>,
    val decrease: List<NaturePokeathlonStatAffect>
)

data class NaturePokeathlonStatAffect(
    val maxChange: Int,
    val nature: NamedApiResource
)

@Entity
data class Pokemon(
    @PrimaryKey
    val id: Int,
    val name: String,
    @Ignore
    val baseExperience: Int = 0,
    @Ignore
    val height: Int = 0,
    @Ignore
    val isDefault: Boolean = false,
    @Ignore
    val order: Int = 0,
    @Ignore
    val weight: Int = 0,
    @Ignore
    val species: NamedApiResource,
    @Ignore
    val abilities: List<PokemonAbility> = emptyList(),
    @Ignore
    val forms: List<NamedApiResource> = emptyList(),
    @Ignore
    val gameIndices: List<VersionGameIndex> = emptyList(),
    @Ignore
    val heldItems: List<PokemonHeldItem> = emptyList(),
    @Ignore
    val moves: List<PokemonMove> = emptyList(),
    @Ignore
    val stats: List<PokemonStat> = emptyList(),
    @Ignore
    val types: List<PokemonType> = emptyList(),
    @Ignore
    val sprites: PokemonSprites?
) {
    constructor(id: Int, name: String) : this(
        id,
        name,
        0,
        0,
        false,
        0,
        0,
        NamedApiResource("",""),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        null,
    )
}

data class PokemonSprites(
    val backDefault: String?,
    val backShiny: String?,
    val frontDefault: String?,
    val frontShiny: String?,
    val backFemale: String?,
    val backShinyFemale: String?,
    val frontFemale: String?,
    val frontShinyFemale: String?,
    val other: PokemonSpritesOther

)

data class PokemonSpritesOther(
    val dreamWorld: PokemonSpritesDreamWorld,
    val home: PokemonSpritesHome,
    @SerializedName("official-artwork")
    val officialArtwork: PokemonSpritesOfficialArtwork
)

data class PokemonSpritesDreamWorld(
    val frontDefault: String,
    val frontFemale: String?
)

data class PokemonSpritesHome(
    val frontDefault: String,
    val frontFemale: String?,
    val frontShiny: String,
    val frontShinyFemale: String?
)

data class PokemonSpritesOfficialArtwork(
    val frontDefault: String,
    val frontShiny: String
)

data class PokemonAbility(
    val isHidden: Boolean,
    val slot: Int,
    val ability: NamedApiResource
)

data class PokemonHeldItem(
    val item: NamedApiResource,
    val versionDetails: List<PokemonHeldItemVersion>
)

data class PokemonHeldItemVersion(
    val version: NamedApiResource,
    val rarity: Int
)

data class PokemonMove(
    val move: NamedApiResource,
    val versionGroupDetails: List<PokemonMoveVersion>
)

data class PokemonMoveVersion(
    val moveLearnMethod: NamedApiResource,
    val versionGroup: NamedApiResource,
    val levelLearnedAt: Int
)

data class PokemonStat(
    val stat: NamedApiResource,
    val effort: Int,
    val baseStat: Int
)

data class PokemonType(
    val slot: Int,
    val type: NamedApiResource
)

data class LocationAreaEncounter(
    val locationArea: NamedApiResource,
    val versionDetails: List<VersionEncounterDetail>
)

data class PokemonColor(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val pokemonSpecies: List<NamedApiResource>
)

data class PokemonForm(
    val id: Int,
    val name: String,
    val order: Int,
    val formOrder: Int,
    val isDefault: Boolean,
    val isBattleOnly: Boolean,
    val isMega: Boolean,
    val formName: String,
    val pokemon: NamedApiResource,
    val versionGroup: NamedApiResource,
    val sprites: PokemonFormSprites,
    val formNames: List<Name>
)

data class PokemonFormSprites(
    val backDefault: String?,
    val backShiny: String?,
    val frontDefault: String?,
    val frontShiny: String?
)

data class PokemonHabitat(
    val id: Int,
    val name: String,
    val names: List<Name>,
    val pokemonSpecies: List<NamedApiResource>
)

data class PokemonShape(
    val id: Int,
    val name: String,
    val awesomeNames: List<AwesomeName>,
    val names: List<Name>,
    val pokemonSpecies: List<NamedApiResource>
)

data class AwesomeName(
    val awesomeName: String,
    val language: NamedApiResource
)

data class PokemonSpecies(
    val id: Int,
    val name: String,
    val order: Int,
    val genderRate: Int,
    val captureRate: Int,
    val baseHappiness: Int,
    val isBaby: Boolean,
    val isLegendary: Boolean,
    val isMythical: Boolean,
    val hatchCounter: Int,
    val hasGenderDifferences: Boolean,
    val formsSwitchable: Boolean,
    val growthRate: NamedApiResource,
    val pokedexNumbers: List<PokemonSpeciesDexEntry>,
    val eggGroups: List<NamedApiResource>,
    val color: NamedApiResource,
    val shape: NamedApiResource,
    val evolvesFromSpecies: NamedApiResource?,
    val evolutionChain: ApiResource,
    val habitat: NamedApiResource?,
    val generation: NamedApiResource,
    val names: List<Name>,
    val palParkEncounters: List<PalParkEncounterArea>,
    val formDescriptions: List<Description>,
    val genera: List<Genus>,
    val varieties: List<PokemonSpeciesVariety>,
    val flavorTextEntries: List<PokemonSpeciesFlavorText>
)

data class PokemonSpeciesFlavorText(
    val flavorText: String,
    val language: NamedApiResource,
    val version: NamedApiResource
)

data class Genus(
    val genus: String,
    val language: NamedApiResource
)

data class PokemonSpeciesDexEntry(
    val entryNumber: Int,
    val pokedex: NamedApiResource
)

data class PalParkEncounterArea(
    val baseScore: Int,
    val rate: Int,
    val area: NamedApiResource
)

data class PokemonSpeciesVariety(
    val isDefault: Boolean,
    val pokemon: NamedApiResource
)

data class Stat(
    val id: Int,
    val name: String,
    val gameIndex: Int,
    val isBattleOnly: Boolean,
    val affectingMoves: MoveStatAffectSets,
    val affectingNatures: NatureStatAffectSets,
    val characteristics: List<ApiResource>,
    val moveDamageClass: NamedApiResource?,
    val names: List<Name>
)

data class MoveStatAffectSets(
    val increase: List<MoveStatAffect>,
    val decrease: List<MoveStatAffect>
)

data class MoveStatAffect(
    val change: Int,
    val move: NamedApiResource
)

data class NatureStatAffectSets(
    val increase: List<NamedApiResource>,
    val decrease: List<NamedApiResource>
)

data class Type(
    val id: Int,
    val name: String,
    val damageRelations: TypeRelations,
    val gameIndices: List<GenerationGameIndex>,
    val generation: NamedApiResource,
    val moveDamageClass: NamedApiResource?,
    val names: List<Name>,
    val pokemon: List<TypePokemon>,
    val moves: List<NamedApiResource>
)

data class TypePokemon(
    val slot: Int,
    val pokemon: NamedApiResource
)

data class TypeRelations(
    val noDamageTo: List<NamedApiResource>,
    val halfDamageTo: List<NamedApiResource>,
    val doubleDamageTo: List<NamedApiResource>,
    val noDamageFrom: List<NamedApiResource>,
    val halfDamageFrom: List<NamedApiResource>,
    val doubleDamageFrom: List<NamedApiResource>
)