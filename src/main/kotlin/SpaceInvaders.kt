import javafx.animation.Animation
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.*
import javafx.scene.media.AudioClip
import javafx.scene.paint.Color
import javafx.scene.text.*
import javafx.stage.Stage
import java.time.Instant
import kotlin.random.Random
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.concurrent.timer


// =====================================================================================================================
// create a class for user bullet
// =====================================================================================================================

class UserBullet(public val xval: Double, public val yval: Double, public val time_launched: Long) {


    val shoot = AudioSystem.getAudioInputStream(File("${System.getProperty("user.dir")}/src/main/resources/Audio/shoot.wav"))
    val shootaudio = AudioSystem.getClip()

    //val shoot = Media("${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/")

    /*
    val shoot = Media("${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/")
    val shootaudio = MediaPlayer(shoot)

     */

    val img = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/user_bullet.png/")
    val imageview = ImageView().apply {
        image = img
        fitWidth = 3.0
        isPreserveRatio = true
        isSmooth = true
        isCache = true
        x = xval
        y = yval
    }

}








// =====================================================================================================================
// create a class for alien bullet
// =====================================================================================================================

class AlienBullet(public val xval: Double, public val yval: Double, public val time_launched: Long, public val img: Image, public val sound: File) {

    val shoot = AudioSystem.getAudioInputStream(sound)
    val shootaudio = AudioSystem.getClip()

    val imageview = ImageView().apply {
        image = img
        fitWidth = 10.0
        isPreserveRatio = true
        isSmooth = true
        isCache = true
        x = xval
        y = yval
    }


}








// =====================================================================================================================
// create a class for user ship
// =====================================================================================================================

class UserShip(public val left_pressed: Boolean, public  val right_pressed: Boolean) {

    val shoot1 = AudioSystem.getAudioInputStream(File("${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/"))
    val killedaudio1 = AudioSystem.getClip()

    val shoot2 = AudioSystem.getAudioInputStream(File("${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/"))
    val killedaudio2 = AudioSystem.getClip()

    val shoot3 = AudioSystem.getAudioInputStream(File("${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/"))
    val killedaudio3 = AudioSystem.getClip()


    val img = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/user_ship.png/")
    val imageview = ImageView().apply {
        image = img
        fitWidth = 60.0
        isPreserveRatio = true
        isSmooth = true
        isCache = true
        x = 425.0
        y = 520.0
    }


}








// =====================================================================================================================
// create a class for aliens
// =====================================================================================================================

class NewAlien(public val initialx: Double, public val initialy: Double, public val img: Image) {

    var dead = false

    val shoot = AudioSystem.getAudioInputStream(File("${System.getProperty("user.dir")}/src/main/resources/Audio/invaderkilled.wav/"))
    val shootaudio = AudioSystem.getClip()




    val imageview = ImageView().apply {
        image = img
        fitWidth = 45.0
        isPreserveRatio = true
        isSmooth = true
        isCache = true
        x = initialx
        y = initialy
    }


}








// =====================================================================================================================
// main function
// =====================================================================================================================

class SpaceInvaders : Application() {

    override fun start(stage: Stage) {

        // ========================================= global variables ==================================================

        //val file = "${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/"
        //val shoot = Media("${System.getProperty("user.dir")}/src/main/resources/Audio/explosion.wav/")



        var playerSpeed = 3.0
        var playerBulletSpeed = 6.0
        var initialEnemySpeed = 0.5
        var enemySpeed = 0.5
        var enemyVerticalSpeed = 10.0
        var alienShootInterval = 2000
        // track score
        var score = 0
        var lives = 2
        var level = 1
        var playing = false
        var played_from1 = false
        var played_from2 = false

        // track for user ship
        var left_pressed = false
        var right_pressed = false
        var space_pressed = false
        var missle_launched = false

        // track bullets
        //val bulletlist = ListView<UserBullet>()
        //var numbulletslaunched = 0
        var lastbullettime = Instant.now().toEpochMilli()
        var lastalienbullettime = Instant.now().toEpochMilli()

        // set up alien columns
        var curimg = "30pt_alien.png/"
        var path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")


        // block of aliens
        var alienblock1 = ListView<ListView<NewAlien>>()
        var col0Aliens1 = ListView<NewAlien>()
        var col1Aliens1 = ListView<NewAlien>()
        var col2Aliens1 = ListView<NewAlien>()
        var col3Aliens1 = ListView<NewAlien>()
        var col4Aliens1 = ListView<NewAlien>()
        var col5Aliens1 = ListView<NewAlien>()
        var col6Aliens1 = ListView<NewAlien>()
        var col7Aliens1 = ListView<NewAlien>()
        var col8Aliens1 = ListView<NewAlien>()
        var col9Aliens1 = ListView<NewAlien>()

        alienblock1.items.add(col0Aliens1)
        alienblock1.items.add(col1Aliens1)
        alienblock1.items.add(col2Aliens1)
        alienblock1.items.add(col3Aliens1)
        alienblock1.items.add(col4Aliens1)
        alienblock1.items.add(col5Aliens1)
        alienblock1.items.add(col6Aliens1)
        alienblock1.items.add(col7Aliens1)
        alienblock1.items.add(col8Aliens1)
        alienblock1.items.add(col9Aliens1)

        var y = 0.0

        for (i in 0 until 5) {
            var x = 205.0
            if (i == 1) {
                y += 50.0
            }
            if (i == 2) {
                y += 40.0
            }
            if ((i == 1) || (i == 2)) {
                curimg = "20pt_alien.png/"
                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
            }
            else if ((i == 3) || (i == 4)) {
                y += 40.0
                curimg = "10pt_alien.png/"
                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
            }
            for (j in 0 until 10) {
                var newalien = NewAlien(x, y, path)
                alienblock1.items[j].items.add(newalien)
                x += 50.0
            }

        }


        // set up alien columns
        curimg = "30pt_alien.png/"
        path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")

        // block of aliens
        var alienblock2 = ListView<ListView<NewAlien>>()
        var col0Aliens2 = ListView<NewAlien>()
        var col1Aliens2 = ListView<NewAlien>()
        var col2Aliens2 = ListView<NewAlien>()
        var col3Aliens2 = ListView<NewAlien>()
        var col4Aliens2 = ListView<NewAlien>()
        var col5Aliens2 = ListView<NewAlien>()
        var col6Aliens2 = ListView<NewAlien>()
        var col7Aliens2 = ListView<NewAlien>()
        var col8Aliens2 = ListView<NewAlien>()
        var col9Aliens2 = ListView<NewAlien>()

        alienblock2.items.add(col0Aliens2)
        alienblock2.items.add(col1Aliens2)
        alienblock2.items.add(col2Aliens2)
        alienblock2.items.add(col3Aliens2)
        alienblock2.items.add(col4Aliens2)
        alienblock2.items.add(col5Aliens2)
        alienblock2.items.add(col6Aliens2)
        alienblock2.items.add(col7Aliens2)
        alienblock2.items.add(col8Aliens2)
        alienblock2.items.add(col9Aliens2)

        y = 0.0

        for (i in 0 until 5) {
            var x = 205.0
            if (i == 1) {
                y += 50.0
            }
            if (i == 2) {
                y += 40.0
            }
            if ((i == 1) || (i == 2)) {
                curimg = "20pt_alien.png/"
                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
            }
            else if ((i == 3) || (i == 4)) {
                y += 40.0
                curimg = "10pt_alien.png/"
                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
            }
            for (j in 0 until 10) {
                var newalien = NewAlien(x, y, path)
                alienblock2.items[j].items.add(newalien)
                x += 50.0
            }

        }



        // set up alien columns
        curimg = "30pt_alien.png/"
        path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")

        // block of aliens
        var alienblock3 = ListView<ListView<NewAlien>>()
        var col0Aliens3 = ListView<NewAlien>()
        var col1Aliens3 = ListView<NewAlien>()
        var col2Aliens3 = ListView<NewAlien>()
        var col3Aliens3 = ListView<NewAlien>()
        var col4Aliens3 = ListView<NewAlien>()
        var col5Aliens3 = ListView<NewAlien>()
        var col6Aliens3 = ListView<NewAlien>()
        var col7Aliens3 = ListView<NewAlien>()
        var col8Aliens3 = ListView<NewAlien>()
        var col9Aliens3 = ListView<NewAlien>()

        alienblock3.items.add(col0Aliens3)
        alienblock3.items.add(col1Aliens3)
        alienblock3.items.add(col2Aliens3)
        alienblock3.items.add(col3Aliens3)
        alienblock3.items.add(col4Aliens3)
        alienblock3.items.add(col5Aliens3)
        alienblock3.items.add(col6Aliens3)
        alienblock3.items.add(col7Aliens3)
        alienblock3.items.add(col8Aliens3)
        alienblock3.items.add(col9Aliens3)

        y = 0.0

        for (i in 0 until 5) {
            var x = 205.0
            if (i == 1) {
                y += 50.0
            }
            if (i == 2) {
                y += 40.0
            }
            if ((i == 1) || (i == 2)) {
                curimg = "20pt_alien.png/"
                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
            }
            else if ((i == 3) || (i == 4)) {
                y += 40.0
                curimg = "10pt_alien.png/"
                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
            }
            for (j in 0 until 10) {
                var newalien = NewAlien(x, y, path)
                alienblock3.items[j].items.add(newalien)
                x += 50.0
            }

        }




        // track most left and right in alien block
        var mostleftalienx = 205.0
        var mostrightalienx = 655.0

        // track boundaries
        val leftxboundary = 20.0
        val rightxboundary = 835.0

        // track which columns have no aliens
        var hasAliens = ListView<Boolean>()
        var aliensincol = ListView<Int>()
        for (i in 0 until 10) {
            hasAliens.items.add(true)
            aliensincol.items.add(5)
        }

        // most right and left alien col alive
        var mostleftcol = 0
        var mostrightcol = 9

        // track direction of alien block
        var aliensmoveright = true

        var alienindices = ListView<Pair<Int, Int>>()
        var livealiens = 0
        for (i in 0 until 10) {
            for (j in 0 until 5) {
                alienindices.items.add(Pair<Int, Int>(i, j))
                livealiens += 1
            }
        }


        // audio
        //val shoot = AudioSystem.getAudioInputStream(javaClass.getResourceAsStream("/resources/Audio/shoot.wav"))
        //val shootaudio = AudioSystem.getClip()
        //val audioinputstream = AudioInputStream(null)
        //val shoot = AudioSystem.getAudioInputStream(File("${System.getProperty("user.dir")}/src/main/resources/Audio/shoot.wav"))
        //val shootaudio = AudioSystem.getClip()
        //shootaudio.open(shoot)



















        // ========================================== homepane setup ===================================================


        val tophomepane = Pane()
        tophomepane.setOnMouseClicked { println("top home PANE clicked") }

        tophomepane.background = Background(BackgroundFill(Color.valueOf("F4F4F4"), null, null))

        // set up the title
        val siTitle = ImageView()
        siTitle.image = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/si_logo.png/")
        siTitle.fitWidth = 400.0
        siTitle.isPreserveRatio = true
        siTitle.isSmooth = true
        siTitle.isCache = true
        siTitle.x = 250.0
        siTitle.y = 40.0

        // add it to the homepane
        tophomepane.children.add( siTitle )


        val centrehomepane = Pane()
        centrehomepane.setOnMouseClicked { println("centre home pane clicked") }

        centrehomepane.background = Background(BackgroundFill(Color.valueOf("F4F4F4"), null, null))

        val instructions = ImageView()
        instructions.image = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/instructions.png/")
        instructions.fitWidth = 300.0
        instructions.isPreserveRatio = true
        instructions.isSmooth = true
        instructions.isCache = true
        instructions.x = 305.0
        instructions.y = 75.0


        // add it to homepane
        centrehomepane.children.add(instructions)



        // bothomepane = personal information
        val bothomepane = Pane()
        bothomepane.prefHeight = 10.0

        bothomepane.background = Background(BackgroundFill(Color.valueOf("F4F4F4"), null, null))

        bothomepane.setOnMouseClicked { println("bottom home pane clicked") }
        val info = Text("Implemented by Jessica Chan (20901141) for CS 349, Univeristy of Waterloo, S23").apply {
            FontWeight.LIGHT
            // textAlignment = TextAlignment.CENTER
            x = 265.0
        }


        bothomepane.children.add(info)


        val homepaneGC = BorderPane()
        homepaneGC.top = tophomepane
        homepaneGC.center = centrehomepane
        homepaneGC.bottom = bothomepane


        val homescene = Scene(homepaneGC, 900.0, 600.0)














        // ======================================== level 1 pane setup =================================================

        val topLevelOne = VBox()
        topLevelOne.prefHeight = 40.0
        topLevelOne.alignment = Pos.CENTER
        topLevelOne.setOnMouseClicked { println("Top Game Pane clicked")}

        var printscore = Text("Score: ${score}                                                                                  Lives: ${lives}     Level: ${level}")
        printscore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18.0));
        printscore.fill = Color.WHITE

        topLevelOne.children.add(printscore)


        /////////////////////////////////////////////////////////

        val gamepane1 = Pane()

        // set up user ship
        var usership1 = UserShip(left_pressed, right_pressed)

        gamepane1.children.add(usership1.imageview)


        for (i in 0 until 10) {
            for (j in 0 until 5) {
                gamepane1.children.add(alienblock1.items[i].items[j].imageview)
            }
        }

        /*
        for (i in 0 until 5) {
            gamepane1.children.add(alienblock1.items[0].items[i].imageview)
        }

         */

        val gamepane1GC = BorderPane()
        gamepane1GC.top = topLevelOne
        gamepane1GC.center = gamepane1

        gamepane1GC.background = Background(BackgroundFill(Color.valueOf("000000"), null, null))


        val levelOneScene = Scene(gamepane1GC, 900.0, 600.0)


        //////////////////////////////////////////////////////////


        // ============================================ final scene ====================================================
        val finalpane = VBox()
        finalpane.prefHeight = 600.0
        finalpane.alignment = Pos.CENTER




        var printgameover = Text("")
        if (livealiens == 0) {
            printgameover = Text("YOU WON!")
        }
        else {
            printgameover = Text("GAME OVER!")
        }

        printgameover.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 24.0));
        printgameover.fill = Color.WHITE

        printscore = Text("\nLevel: ${level}               Final Score: ${score}\n")
        printscore.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
        printscore.fill = Color.WHITE

        finalpane.children.add(printgameover)
        finalpane.children.add(printscore)


        val printnewgame = Text("ENTER - Start New Game")
        val printbacktohome = Text("H - Back to Home")
        val printquit = Text("Q - Quit Game")
        val print123 = Text("1 or 2 or 3 - Start New Game at a specific level")

        printnewgame.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
        printnewgame.fill = Color.WHITE

        printbacktohome.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
        printbacktohome.fill = Color.WHITE

        printquit.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
        printquit.fill = Color.WHITE

        print123.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
        print123.fill = Color.WHITE




        finalpane.children.add(printnewgame)
        finalpane.children.add(printbacktohome)
        finalpane.children.add(printquit)
        finalpane.children.add(print123)




        /////////////////////////////////////////////////////////

        val final = BorderPane()
        final.center = finalpane

        final.background = Background(BackgroundFill(Color.valueOf("000000"), null, null))

        val finalscene = Scene(final, 900.0, 600.0)















        // =========================================== functionality ===================================================



        /////////////////////////////// moving user ship //////////////////////////////


        levelOneScene.setOnKeyPressed { event ->
            if ((event.code == KeyCode.A) || (event.code == KeyCode.LEFT)) {
                println("pressed A key to move left")
                right_pressed = false
                left_pressed = true
            }

            else if ((event.code == KeyCode.D) || (event.code == KeyCode.RIGHT)) {
                println("pressed D key to move right")
                left_pressed = false
                right_pressed = true
            }
            if (event.code == KeyCode.SPACE) {
                println("released SPACE key to shoot")
                space_pressed = true
            }
            /*
            if (event.code == KeyCode.SPACE) {
                println("pressed SPACE key to shoot")
                space_pressed = false
            }

             */
        }

        levelOneScene.setOnKeyReleased { event ->
            if ((event.code == KeyCode.A) || (event.code == KeyCode.LEFT)) {
                println("pressed A key to move left")
                left_pressed = false
            }
            if ((event.code == KeyCode.D) || (event.code == KeyCode.RIGHT)) {
                println("pressed D key to move right")
                right_pressed = false
            }

            if (event.code == KeyCode.SPACE) {
                println("released SPACE key to shoot")
                space_pressed = false
            }
        }



        val timer1: AnimationTimer = object : AnimationTimer() {
            override fun handle(now: Long) {


                if (playing) {
                    // alien block moving
                    // find most left and right cols
                    for (i in 0 until 10) {
                        if (hasAliens.items[i]) {
                            mostleftcol = i
                            break
                        }
                    }
                    for (i in 9 downTo  0) {
                        if (hasAliens.items[i]) {
                            mostrightcol = i
                            break
                        }
                    }
                    var adjustedy = true
                    if (aliensmoveright && ((alienblock1.items[mostrightcol].items[0].imageview.x + enemySpeed) <= rightxboundary)) {
                        adjustedy = false
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                alienblock1.items[i].items[j].imageview.x += enemySpeed
                            }
                        }
                    }
                    if (((alienblock1.items[mostrightcol].items[0].imageview.x + enemySpeed) > rightxboundary) && !adjustedy) {
                        adjustedy = true
                        aliensmoveright = false
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                alienblock1.items[i].items[j].imageview.y += enemyVerticalSpeed
                            }
                        }

                    }
                    if (!aliensmoveright && ((alienblock1.items[mostleftcol].items[0].imageview.x - enemySpeed) >= leftxboundary)) {
                        adjustedy = false
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                alienblock1.items[i].items[j].imageview.x -= enemySpeed
                            }
                        }
                    }
                    if (((alienblock1.items[mostleftcol].items[0].imageview.x - enemySpeed) < leftxboundary) && !adjustedy) {
                        adjustedy = true
                        aliensmoveright = true
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                alienblock1.items[i].items[j].imageview.y += enemyVerticalSpeed
                            }
                        }
                    }

                    // check if won
                    if (livealiens == 0) {

                        stop()

                        usership1.imageview.imageProperty().set(null)


                        playing = false
                        gamepane1.children.clear()
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                alienblock1.items[i].items[j].imageview.imageProperty().set(null)
                            }
                        }
                        col0Aliens1.items.clear()
                        col1Aliens1.items.clear()
                        col2Aliens1.items.clear()
                        col3Aliens1.items.clear()
                        col4Aliens1.items.clear()
                        col5Aliens1.items.clear()
                        col6Aliens1.items.clear()
                        col7Aliens1.items.clear()
                        col8Aliens1.items.clear()
                        col9Aliens1.items.clear()

                        alienblock1.items.clear()
                        alienblock1.items.add(col0Aliens1)
                        alienblock1.items.add(col1Aliens1)
                        alienblock1.items.add(col2Aliens1)
                        alienblock1.items.add(col3Aliens1)
                        alienblock1.items.add(col4Aliens1)
                        alienblock1.items.add(col5Aliens1)
                        alienblock1.items.add(col6Aliens1)
                        alienblock1.items.add(col7Aliens1)
                        alienblock1.items.add(col8Aliens1)
                        alienblock1.items.add(col9Aliens1)

                        curimg = "30pt_alien.png/"
                        path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                        y = 0.0
                        for (i in 0 until 5) {
                            var x = 205.0
                            if (i == 1) {
                                y += 50.0
                            }
                            if (i == 2) {
                                y += 40.0
                            }
                            if ((i == 1) || (i == 2)) {
                                curimg = "20pt_alien.png/"
                                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                            }
                            else if ((i == 3) || (i == 4)) {
                                y += 40.0
                                curimg = "10pt_alien.png/"
                                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                            }
                            for (j in 0 until 10) {
                                var newalien = NewAlien(x, y, path)
                                alienblock1.items[j].items.add(newalien)
                                x += 50.0
                            }

                        }


                        gamepane1.children.add(usership1.imageview)
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                gamepane1.children.add(alienblock1.items[i].items[j].imageview)
                            }
                        }


                        // reset global variables for game
                        enemySpeed = initialEnemySpeed


                        usership1 = UserShip(left_pressed, right_pressed)
                        gamepane1.children.add(usership1.imageview)


                        hasAliens.items.clear()
                        aliensincol.items.clear()
                        for (i in 0 until 10) {
                            hasAliens.items.add(true)
                            aliensincol.items.add(5)
                        }

                        mostleftcol = 0
                        mostrightcol = 9

                        alienindices.items.clear()
                        livealiens = 0
                        for (i in 0 until 10) {
                            for (j in 0 until 5) {
                                alienindices.items.add(Pair<Int, Int>(i, j))
                                livealiens += 1
                            }
                        }




                        stage.scene = finalscene

                        printgameover = Text("YOU WON!")

                        printgameover.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 24.0))
                        printgameover.fill = Color.WHITE

                        printscore = Text("\nLevel: ${level}       Final Score: ${score}\n")
                        printscore.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
                        printscore.fill = Color.WHITE

                        finalpane.children.clear()

                        finalpane.children.add(printgameover)
                        finalpane.children.add(printscore)
                        finalpane.children.add(printnewgame)
                        finalpane.children.add(printbacktohome)
                        finalpane.children.add(printquit)
                        finalpane.children.add(print123)



                    }










                    for (i in mostleftcol until mostrightcol) {
                        for (j in 4 downTo 0) {
                            // handle alien hitting bottom
                            if ((!alienblock1.items[i].items[j].dead) && (alienblock1.items[i].items[j].imageview.y >= (545.0))) {

                                lives = 0

                                stop()

                                usership1.imageview.imageProperty().set(null)

                                usership1.killedaudio1.open(usership1.shoot1)
                                usership1.killedaudio1.start()

                                playing = false
                                gamepane1.children.clear()
                                for (i in 0 until 10) {
                                    for (j in 0 until 5) {
                                        alienblock1.items[i].items[j].imageview.imageProperty().set(null)
                                    }
                                }
                                col0Aliens1.items.clear()
                                col1Aliens1.items.clear()
                                col2Aliens1.items.clear()
                                col3Aliens1.items.clear()
                                col4Aliens1.items.clear()
                                col5Aliens1.items.clear()
                                col6Aliens1.items.clear()
                                col7Aliens1.items.clear()
                                col8Aliens1.items.clear()
                                col9Aliens1.items.clear()

                                alienblock1.items.clear()
                                alienblock1.items.add(col0Aliens1)
                                alienblock1.items.add(col1Aliens1)
                                alienblock1.items.add(col2Aliens1)
                                alienblock1.items.add(col3Aliens1)
                                alienblock1.items.add(col4Aliens1)
                                alienblock1.items.add(col5Aliens1)
                                alienblock1.items.add(col6Aliens1)
                                alienblock1.items.add(col7Aliens1)
                                alienblock1.items.add(col8Aliens1)
                                alienblock1.items.add(col9Aliens1)

                                curimg = "30pt_alien.png/"
                                path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                y = 0.0
                                for (i in 0 until 5) {
                                    var x = 205.0
                                    if (i == 1) {
                                        y += 50.0
                                    }
                                    if (i == 2) {
                                        y += 40.0
                                    }
                                    if ((i == 1) || (i == 2)) {
                                        curimg = "20pt_alien.png/"
                                        path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                    }
                                    else if ((i == 3) || (i == 4)) {
                                        y += 40.0
                                        curimg = "10pt_alien.png/"
                                        path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                    }
                                    for (j in 0 until 10) {
                                        var newalien = NewAlien(x, y, path)
                                        alienblock1.items[j].items.add(newalien)
                                        x += 50.0
                                    }

                                }


                                gamepane1.children.add(usership1.imageview)
                                for (i in 0 until 10) {
                                    for (j in 0 until 5) {
                                        gamepane1.children.add(alienblock1.items[i].items[j].imageview)
                                    }
                                }


                                // reset global variables for game
                                enemySpeed = initialEnemySpeed


                                usership1 = UserShip(left_pressed, right_pressed)
                                gamepane1.children.add(usership1.imageview)


                                hasAliens.items.clear()
                                aliensincol.items.clear()
                                for (i in 0 until 10) {
                                    hasAliens.items.add(true)
                                    aliensincol.items.add(5)
                                }

                                mostleftcol = 0
                                mostrightcol = 9

                                alienindices.items.clear()
                                livealiens = 0
                                for (i in 0 until 10) {
                                    for (j in 0 until 5) {
                                        alienindices.items.add(Pair<Int, Int>(i, j))
                                        livealiens += 1
                                    }
                                }

                                left_pressed = false
                                right_pressed = false
                                space_pressed = false

                                aliensmoveright = true




                                stage.scene = finalscene

                                printgameover = Text("GAME OVER!")

                                printgameover.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 24.0))
                                printgameover.fill = Color.WHITE

                                printscore = Text("\nLevel: ${level}       Final Score: ${score}\n")
                                printscore.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 16.0));
                                printscore.fill = Color.WHITE

                                finalpane.children.clear()

                                finalpane.children.add(printgameover)
                                finalpane.children.add(printscore)
                                finalpane.children.add(printnewgame)
                                finalpane.children.add(printbacktohome)
                                finalpane.children.add(printquit)
                                finalpane.children.add(print123)


                            }
                        }
                    }






                    // handle alien hitting ship
                    for (i in mostleftcol until mostrightcol) {
                        for (j in 4 downTo 0) {
                            if ((!alienblock1.items[i].items[j].dead) && (alienblock1.items[i].items[j].imageview.y >= usership1.imageview.y) &&
                                ((alienblock1.items[i].items[j].imageview.x + 45.0) <= usership1.imageview.x) && (usership1.imageview.x <= (alienblock1.items[i].items[j].imageview.x + 60.0))
                            ) {


                                lives -= 1

                                if (lives == 0) {

                                    stop()

                                    usership1.imageview.imageProperty().set(null)

                                    usership1.killedaudio3.open(usership1.shoot3)
                                    usership1.killedaudio3.start()

                                    playing = false
                                    gamepane1.children.clear()
                                    for (i in 0 until 10) {
                                        for (j in 0 until 5) {
                                            alienblock1.items[i].items[j].imageview.imageProperty().set(null)
                                        }
                                    }
                                    col0Aliens1.items.clear()
                                    col1Aliens1.items.clear()
                                    col2Aliens1.items.clear()
                                    col3Aliens1.items.clear()
                                    col4Aliens1.items.clear()
                                    col5Aliens1.items.clear()
                                    col6Aliens1.items.clear()
                                    col7Aliens1.items.clear()
                                    col8Aliens1.items.clear()
                                    col9Aliens1.items.clear()

                                    alienblock1.items.clear()
                                    alienblock1.items.add(col0Aliens1)
                                    alienblock1.items.add(col1Aliens1)
                                    alienblock1.items.add(col2Aliens1)
                                    alienblock1.items.add(col3Aliens1)
                                    alienblock1.items.add(col4Aliens1)
                                    alienblock1.items.add(col5Aliens1)
                                    alienblock1.items.add(col6Aliens1)
                                    alienblock1.items.add(col7Aliens1)
                                    alienblock1.items.add(col8Aliens1)
                                    alienblock1.items.add(col9Aliens1)

                                    curimg = "30pt_alien.png/"
                                    path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                    y = 0.0
                                    for (i in 0 until 5) {
                                        var x = 205.0
                                        if (i == 1) {
                                            y += 50.0
                                        }
                                        if (i == 2) {
                                            y += 40.0
                                        }
                                        if ((i == 1) || (i == 2)) {
                                            curimg = "20pt_alien.png/"
                                            path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                        }
                                        else if ((i == 3) || (i == 4)) {
                                            y += 40.0
                                            curimg = "10pt_alien.png/"
                                            path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                        }
                                        for (j in 0 until 10) {
                                            var newalien = NewAlien(x, y, path)
                                            alienblock1.items[j].items.add(newalien)
                                            x += 50.0
                                        }

                                    }


                                    gamepane1.children.add(usership1.imageview)
                                    for (i in 0 until 10) {
                                        for (j in 0 until 5) {
                                            gamepane1.children.add(alienblock1.items[i].items[j].imageview)
                                        }
                                    }


                                    // reset global variables for game
                                    enemySpeed = initialEnemySpeed


                                    usership1 = UserShip(left_pressed, right_pressed)
                                    gamepane1.children.add(usership1.imageview)


                                    hasAliens.items.clear()
                                    aliensincol.items.clear()
                                    for (i in 0 until 10) {
                                        hasAliens.items.add(true)
                                        aliensincol.items.add(5)
                                    }

                                    mostleftcol = 0
                                    mostrightcol = 9

                                    alienindices.items.clear()
                                    livealiens = 0
                                    for (i in 0 until 10) {
                                        for (j in 0 until 5) {
                                            alienindices.items.add(Pair<Int, Int>(i, j))
                                            livealiens += 1
                                        }
                                    }


                                    left_pressed = false
                                    right_pressed = false
                                    space_pressed = false

                                    aliensmoveright = true





                                    stage.scene = finalscene

                                    printgameover = Text("GAME OVER!")

                                    printgameover.setFont(
                                        Font.font(
                                            "verdana",
                                            FontWeight.BOLD,
                                            FontPosture.REGULAR,
                                            24.0
                                        )
                                    )
                                    printgameover.fill = Color.WHITE

                                    printscore = Text("\nLevel: ${level}       Final Score: ${score}\n")
                                    printscore.setFont(
                                        Font.font(
                                            "verdana",
                                            FontWeight.NORMAL,
                                            FontPosture.REGULAR,
                                            16.0
                                        )
                                    );
                                    printscore.fill = Color.WHITE

                                    finalpane.children.clear()

                                    finalpane.children.add(printgameover)
                                    finalpane.children.add(printscore)
                                    finalpane.children.add(printnewgame)
                                    finalpane.children.add(printbacktohome)
                                    finalpane.children.add(printquit)
                                    finalpane.children.add(print123)


                                }


                                else {

                                    // update toppane
                                    printscore = Text("Score: ${score}                                                                                  Lives: ${lives}     Level: ${level}")
                                    printscore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18.0));
                                    printscore.fill = Color.WHITE

                                    topLevelOne.children.clear()
                                    topLevelOne.children.add(printscore)

                                    // kill alien AND user
                                    alienblock1.items[i].items[j].imageview.imageProperty().set(null)
                                    alienblock1.items[i].items[j].dead = true



                                    aliensincol.items[i] -= 1

                                    if (aliensincol.items[i] == 0) {
                                        hasAliens.items[i] = false
                                        if (i == mostleftcol) {
                                            mostleftcol += 1
                                        }
                                        else if (i == mostrightcol + 1) {
                                            mostrightcol -= 1
                                        }
                                    }

                                    var newalienindices = ListView<Pair<Int, Int>>()

                                    for (k in 0 until livealiens) {
                                        if ((alienindices.items[k].first == i) && (alienindices.items[k].second == j)) {
                                            // do nothing
                                        }
                                        else {
                                            newalienindices.items.add(alienindices.items[k])
                                        }
                                    }

                                    alienindices.items.clear()
                                    livealiens -= 1
                                    for (k in 0 until livealiens) {
                                        alienindices.items.add(newalienindices.items[k])
                                    }


                                    // now kill and respawn player
                                    val temp = usership1.imageview.image
                                    usership1.imageview.imageProperty().set(null)

                                    if (lives == 2) {
                                        usership1.killedaudio1.open(usership1.shoot1)
                                        usership1.killedaudio1.start()
                                    }
                                    else if (lives == 1) {
                                        usership1.killedaudio2.open(usership1.shoot2)
                                        usership1.killedaudio2.start()
                                    }

                                    usership1.imageview.x = 425.0
                                    usership1.imageview.y = 520.0

                                    usership1.imageview.image = temp

                                    //usership1 = UserShip(left_pressed, right_pressed)



                                }




                            }
                        }
                    }



                    var newtime = Instant.now().toEpochMilli()

                    if (newtime >= (lastalienbullettime + alienShootInterval)) {



                        var shootingalien = Random.nextInt(0, livealiens)

                        curimg = "pink_bullet.png/"
                        var cursound = "fastinvader1.wav/"

                        if (alienindices.items[shootingalien].second == 0) {
                            curimg = "green_bullet.png/"
                            cursound = "fastinvader3.wav/"
                        }
                        else if ((alienindices.items[shootingalien].second == 1) || (alienindices.items[shootingalien].second == 2)) {
                            curimg = "blue_bullet.png/"
                            cursound = "fastinvader2.wav/"
                        }

                        path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                        var sound = File("${System.getProperty("user.dir")}/src/main/resources/Audio/${cursound}")

                        lastalienbullettime = newtime
                        var newalienbullet = AlienBullet(alienblock1.items[alienindices.items[shootingalien].first].items[alienindices.items[shootingalien].second].imageview.x + 25.0,
                            alienblock1.items[alienindices.items[shootingalien].first].items[alienindices.items[shootingalien].second].imageview.y + 30.0, lastalienbullettime, path, sound)

                        newalienbullet.shootaudio.open(newalienbullet.shoot)
                        newalienbullet.shootaudio.start()

                        val alienbullettimer1: AnimationTimer = object : AnimationTimer() {
                            override fun handle(now: Long) {
                                newalienbullet.imageview.y += playerBulletSpeed



                                // check if user hit by alien bullet
                                if ((usership1.imageview.x <= newalienbullet.imageview.x) && (newalienbullet.imageview.x <= (usership1.imageview.x + 60.0))) {
                                    if ((usership1.imageview.y <= newalienbullet.imageview.y) && (newalienbullet.imageview.y <= (usership1.imageview.y + 10.0))) {

                                        stop()

                                        newalienbullet.imageview.imageProperty().set(null)
                                        val temp = usership1.imageview.image
                                        usership1.imageview.imageProperty().set(null)


                                        lives -= 1

                                        if (lives == 2) {
                                            usership1.killedaudio1.open(usership1.shoot1)
                                            usership1.killedaudio1.start()
                                        }
                                        else if (lives == 1) {
                                            usership1.killedaudio2.open(usership1.shoot2)
                                            usership1.killedaudio2.start()
                                        }

                                        if (lives == 0) {

                                            usership1.killedaudio3.open(usership1.shoot3)
                                            usership1.killedaudio3.start()

                                            playing = false
                                            gamepane1.children.clear()
                                            for (i in 0 until 10) {
                                                for (j in 0 until 5) {
                                                    alienblock1.items[i].items[j].imageview.imageProperty().set(null)
                                                }
                                            }
                                            col0Aliens1.items.clear()
                                            col1Aliens1.items.clear()
                                            col2Aliens1.items.clear()
                                            col3Aliens1.items.clear()
                                            col4Aliens1.items.clear()
                                            col5Aliens1.items.clear()
                                            col6Aliens1.items.clear()
                                            col7Aliens1.items.clear()
                                            col8Aliens1.items.clear()
                                            col9Aliens1.items.clear()

                                            alienblock1.items.clear()
                                            alienblock1.items.add(col0Aliens1)
                                            alienblock1.items.add(col1Aliens1)
                                            alienblock1.items.add(col2Aliens1)
                                            alienblock1.items.add(col3Aliens1)
                                            alienblock1.items.add(col4Aliens1)
                                            alienblock1.items.add(col5Aliens1)
                                            alienblock1.items.add(col6Aliens1)
                                            alienblock1.items.add(col7Aliens1)
                                            alienblock1.items.add(col8Aliens1)
                                            alienblock1.items.add(col9Aliens1)

                                            curimg = "30pt_alien.png/"
                                            path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                            y = 0.0
                                            for (i in 0 until 5) {
                                                var x = 205.0
                                                if (i == 1) {
                                                    y += 50.0
                                                }
                                                if (i == 2) {
                                                    y += 40.0
                                                }
                                                if ((i == 1) || (i == 2)) {
                                                    curimg = "20pt_alien.png/"
                                                    path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                                }
                                                else if ((i == 3) || (i == 4)) {
                                                    y += 40.0
                                                    curimg = "10pt_alien.png/"
                                                    path = Image("${System.getProperty("user.dir")}/src/main/resources/Graphics/${curimg}")
                                                }
                                                for (j in 0 until 10) {
                                                    var newalien = NewAlien(x, y, path)
                                                    alienblock1.items[j].items.add(newalien)
                                                    x += 50.0
                                                }

                                            }


                                            gamepane1.children.add(usership1.imageview)
                                            for (i in 0 until 10) {
                                                for (j in 0 until 5) {
                                                    gamepane1.children.add(alienblock1.items[i].items[j].imageview)
                                                }
                                            }


                                            // reset global variables for game
                                            enemySpeed = initialEnemySpeed


                                            usership1 = UserShip(left_pressed, right_pressed)
                                            gamepane1.children.add(usership1.imageview)


                                            hasAliens.items.clear()
                                            aliensincol.items.clear()
                                            for (i in 0 until 10) {
                                                hasAliens.items.add(true)
                                                aliensincol.items.add(5)
                                            }

                                            mostleftcol = 0
                                            mostrightcol = 9

                                            alienindices.items.clear()
                                            livealiens = 0
                                            for (i in 0 until 10) {
                                                for (j in 0 until 5) {
                                                    alienindices.items.add(Pair<Int, Int>(i, j))
                                                    livealiens += 1
                                                }
                                            }


                                            left_pressed = false
                                            right_pressed = false
                                            space_pressed = false

                                            aliensmoveright = true






                                            stage.scene = finalscene

                                            printgameover = Text("GAME OVER!")

                                            printgameover.setFont(
                                                Font.font(
                                                    "verdana",
                                                    FontWeight.BOLD,
                                                    FontPosture.REGULAR,
                                                    24.0
                                                )
                                            )
                                            printgameover.fill = Color.WHITE

                                            printscore = Text("\nLevel: ${level}       Final Score: ${score}\n")
                                            printscore.setFont(
                                                Font.font(
                                                    "verdana",
                                                    FontWeight.NORMAL,
                                                    FontPosture.REGULAR,
                                                    16.0
                                                )
                                            );
                                            printscore.fill = Color.WHITE

                                            finalpane.children.clear()

                                            finalpane.children.add(printgameover)
                                            finalpane.children.add(printscore)
                                            finalpane.children.add(printnewgame)
                                            finalpane.children.add(printbacktohome)
                                            finalpane.children.add(printquit)
                                            finalpane.children.add(print123)








                                        }


                                        else {

                                            // update toppane
                                            printscore = Text("Score: ${score}                                                                                  Lives: ${lives}     Level: ${level}")
                                            printscore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18.0));
                                            printscore.fill = Color.WHITE

                                            topLevelOne.children.clear()
                                            topLevelOne.children.add(printscore)


                                            // now kill and respawn player
                                            //usership1.imageview.imageProperty().set(null)

                                            usership1.imageview.image = temp

                                            usership1.imageview.x = 425.0
                                            usership1.imageview.y = 520.0

                                            //usership1 = UserShip(left_pressed, right_pressed)



                                        }



                                    }
                                }





                            }
                        }
                        alienbullettimer1.start()
                        gamepane1.children.add(newalienbullet.imageview)
                    }










                }









                // ship handling
                if (usership1.imageview.x > 20.0) {
                    if (left_pressed) {
                        usership1.imageview.x = usership1.imageview.x - playerSpeed
                    }
                }
                if (usership1.imageview.x < 830.0) {
                    if (right_pressed) {
                        usership1.imageview.x = usership1.imageview.x + playerSpeed
                    }
                }
                if (space_pressed) {

                    // create a new missle
                    // check if already created one or not 800ms ago
                    var newbullet = UserBullet(usership1.imageview.x + 29.0, usership1.imageview.y - 5.0, lastbullettime)

                    var newtime = Instant.now().toEpochMilli()

                    if (newtime >= (lastbullettime + 800)) {
                        lastbullettime = newtime
                        newbullet = UserBullet(usership1.imageview.x + 29.0, usership1.imageview.y - 5.0, lastbullettime)
                        //shootaudio.start()
                        //shootaudio?.open(shoot)
                        //shoot.play()
                        newbullet.shootaudio.open(newbullet.shoot)
                        newbullet.shootaudio.start()

                        val bullettimer1: AnimationTimer = object : AnimationTimer() {
                            override fun handle(now: Long) {
                                newbullet.imageview.y -= playerBulletSpeed



                                for (i in mostleftcol until (mostrightcol + 1)) {
                                    for (j in 0 until 5) {

                                        if ((alienblock1.items[i].items[j].imageview.x <= newbullet.imageview.x) && (newbullet.imageview.x <= (alienblock1.items[i].items[j].imageview.x + 50.0))) {
                                            if ((alienblock1.items[i].items[j].imageview.y <= newbullet.imageview.y) && (newbullet.imageview.y <= (alienblock1.items[i].items[j].imageview.y + 35.0)) && !alienblock1.items[i].items[j].dead) {

                                                stop()
                                                newbullet.imageview.imageProperty().set(null)
                                                alienblock1.items[i].items[j].imageview.imageProperty().set(null)

                                                alienblock1.items[i].items[j].shootaudio.open(alienblock1.items[i].items[j].shoot)
                                                alienblock1.items[i].items[j].shootaudio.start()

                                                alienblock1.items[i].items[j].dead = true
                                                aliensincol.items[i] -= 1

                                                if (aliensincol.items[i] == 0) {
                                                    hasAliens.items[i] = false
                                                    if (i == mostleftcol) {
                                                        mostleftcol += 1
                                                    }
                                                    else if (i == mostrightcol + 1) {
                                                        mostrightcol -= 1
                                                    }
                                                }

                                                if (j == 0) {
                                                    score += 30
                                                }
                                                else if (j == 1 || j == 2) {
                                                    score += 20
                                                }
                                                else if (j == 3 || j == 4) {
                                                    score += 10
                                                }

                                                printscore = Text("Score: ${score}                                                                                  Lives: ${lives}     Level: ${level}")
                                                printscore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18.0));
                                                printscore.fill = Color.WHITE

                                                topLevelOne.children.clear()
                                                topLevelOne.children.add(printscore)

                                                enemySpeed += (initialEnemySpeed / 5)

                                                var newalienindices = ListView<Pair<Int, Int>>()

                                                for (k in 0 until livealiens) {
                                                    if ((alienindices.items[k].first == i) && (alienindices.items[k].second == j)) {
                                                        // do nothing
                                                    }
                                                    else {
                                                        newalienindices.items.add(alienindices.items[k])
                                                    }
                                                }

                                                alienindices.items.clear()
                                                livealiens -= 1
                                                for (k in 0 until livealiens) {
                                                    alienindices.items.add(newalienindices.items[k])
                                                }


                                            }
                                        }


                                    }
                                }


                            }
                        }

                        gamepane1.children.add(newbullet.imageview)
                        bullettimer1.start()
                        //shootaudio.start()

                    }






                }





            }
        }









        homescene.setOnKeyPressed { event ->
            lives = 3


            if ((event.code == KeyCode.ENTER) || (event.code == KeyCode.DIGIT1) || (event.code == KeyCode.DIGIT2) || (event.code == KeyCode.DIGIT3)) {
                level = 1
                initialEnemySpeed = 0.5
                alienShootInterval = 2000

                if (event.code == KeyCode.DIGIT2) {
                    level = 2
                    initialEnemySpeed = 0.8
                    alienShootInterval = 1500

                }
                else if (event.code == KeyCode.DIGIT3) {
                    level = 3
                    initialEnemySpeed = 1.5
                    alienShootInterval = 1000

                }
                enemySpeed = initialEnemySpeed
                println("Pressed ENTER or 1 Key")
                playing = true
                stage.scene = levelOneScene
                //alienblock1timer.start()

                printscore = Text("Score: ${score}                                                                                  Lives: ${lives}     Level: ${level}")
                printscore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18.0));
                printscore.fill = Color.WHITE

                topLevelOne.children.clear()
                topLevelOne.children.add(printscore)
                timer1.start()
            }



            else if (event.code == KeyCode.Q) {
                println("Pressed Q - Quit Game")
                Platform.exit()
            }
        }








        // final scene keys
        finalscene.setOnKeyPressed { event ->
            timer1.stop()

            if ((event.code == KeyCode.ENTER) || (event.code == KeyCode.DIGIT1) || (event.code == KeyCode.DIGIT2) || (event.code == KeyCode.DIGIT3)) {
                level = 1
                initialEnemySpeed = 0.5
                alienShootInterval = 2000

                if (event.code == KeyCode.DIGIT2) {
                    level = 2
                    initialEnemySpeed = 0.8
                    alienShootInterval = 1500

                }
                else if (event.code == KeyCode.DIGIT3) {
                    level = 3
                    initialEnemySpeed = 1.5
                    alienShootInterval = 1000

                }
                enemySpeed = initialEnemySpeed
                lives = 3
                score = 0
                println("Pressed ENTER or 1 Key")
                playing = true
                stage.scene = levelOneScene
                //alienblock1timer.start()

                printscore = Text("Score: ${score}                                                                                  Lives: ${lives}     Level: ${level}")
                printscore.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 18.0));
                printscore.fill = Color.WHITE

                topLevelOne.children.clear()
                topLevelOne.children.add(printscore)
                timer1.start()
            }


            else if (event.code == KeyCode.H) {
                stage.scene = homescene
            }


            else if (event.code == KeyCode.Q) {
                Platform.exit()
            }

        }
        //timer.start()



















        // add the scene to the stage and show it
        stage.apply {
            // title bar should contain the name of the program - Space Invaders
            title = "Space Invaders - (c) 2023 Jessica Chan"
            // might change dimensions later, depending on math / size of aliens
            scene = homescene.apply {
                isResizable = false
            }
        }.show()









    }
}


